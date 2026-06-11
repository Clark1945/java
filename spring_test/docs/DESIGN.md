# 設計筆記：Library 範例專案

記錄這個專案在「大量資料寫入」與「REST API 設計」上遇到的問題與解決方案。
技術棧：Spring Boot 3.3.5 / Java 17 / PostgreSQL（Docker）/ Spring Data JPA。

---

## 1. Domain Model

```
Author ──< many-to-many >── Book ──< one-to-many >── BookCopy
```

| Entity     | 關聯                                                            |
|------------|-----------------------------------------------------------------|
| `Book`     | `@ManyToMany` authors（owner，`book_author` join table）；`@OneToMany` copies（`cascade=ALL, orphanRemoval=true`）|
| `Author`   | `@ManyToMany(mappedBy="authors")` books（反向，非 owner）       |
| `BookCopy` | `@ManyToOne(fetch=LAZY)` book；`status` 為 `@Enumerated(STRING)` |

### 設計重點：用 `SEQUENCE` 而非 `IDENTITY` 產生主鍵

```java
@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_seq")
@SequenceGenerator(name = "book_seq", sequenceName = "book_seq", allocationSize = 50)
private Long id;
```

- **問題**：`IDENTITY` 策略下，Hibernate 為了拿到自增 id，**每次 `persist` 都必須立刻送出 INSERT**，導致 JDBC batching 完全失效。
- **解法**：`SEQUENCE` + `allocationSize=50`，id 由序列預先配發，INSERT 可以延後、累積後批次送出。這是大量寫入能批次化的前提。

---

## 2. 大量 Mock 資料（1.7 萬筆）— `LibraryDataInitializer`

啟動時若 DB 為空，塞入 ≈1,000 authors + ≈4,000 books + ≈12,000 copies = **17,015 筆**（另有 8,052 筆 join）。

### 問題 A：persistence context（一級快取）爆掉

如果把所有 entity 一次 `persist` / `saveAll` 而不清理，它們會**全部停留在 persistence context**：

- 記憶體隨資料量線性成長。
- 每次 flush，Hibernate 會對**所有 managed entity** 做 dirty checking，效能接近 O(n²)。

**解法**：用 `EntityManager` 手動分批 flush + clear。

```java
@PersistenceContext
private EntityManager em;

em.persist(entity);
if ((i + 1) % batchSize == 0) {
    em.flush();   // 把這批 SQL 送進 DB
    em.clear();   // 把這批從快取 detach，釋放記憶體
}
```

> 整批 17k 筆種子資料約 **1.9 秒**完成。

### 問題 B：`saveAll` 分批為什麼沒用？

`JpaRepository.saveAll` **本身永遠不會 flush**（內部就是迴圈呼叫 `save` → `em.persist`）。真正觸發 flush 的是**交易 commit**。

| 情境 | 行為 |
|------|------|
| `run()` 有 `@Transactional`（本專案） | `saveAll` 加入外層交易、中間不 commit → 資料**不會**分批送出、快取照樣累積 → 分批沒效果 |
| `run()` 無交易 | 每次 `saveAll` 自帶 `@Transactional` 開新交易並 commit → 會分批送出，但**非原子**、且跨批無法用 `getReference` |

**結論**：要在「單一交易內」做到分批送出 + 釋放記憶體，必須用 `em.flush()` + `em.clear()`，這是 `saveAll` 給不了的控制力。
（若硬要用 repository：`repo.saveAll(batch)` → `repo.flush()` → 仍需 `entityManager.clear()`。）

### 問題 C：掛關聯時的多餘 SELECT

書要關聯作者，若用 `authorRepository.findById(id)` 會多打一次 SELECT；4000 本書 × 多位作者 = 上萬次無謂查詢。尤其前面已 `em.clear()`，作者都 detach 了。

**解法**：`em.getReference()` 只建立含 FK 的 lazy proxy，不查 DB。

```java
book.getAuthors().add(em.getReference(Author.class, authorId));
```

### 為什麼是 `@PersistenceContext` 而不是 `@Autowired`？

`EntityManager` 本身**非 thread-safe**。`@PersistenceContext` 注入的是容器管理的 **shared proxy**，每次操作自動路由到當前交易綁定的真正 EntityManager。這是 JPA 標準做法。

### JDBC batching 設定（`application.properties`）

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/spring_test?reWriteBatchedInserts=true
spring.jpa.properties.hibernate.jdbc.batch_size=50
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true
spring.jpa.show-sql=false   # 1.7 萬筆 INSERT 會把 log 灌爆，關掉
```

- `reWriteBatchedInserts=true`：PostgreSQL JDBC driver 把多筆 INSERT 重寫成單一多值語句，是 PG 端的關鍵加速。
- `order_inserts/updates`：讓同型 entity 的語句相鄰，batch 才湊得滿。

---

## 3. REST API 序列化問題

### 問題 A：直接序列化 JPA entity → 無限遞迴

`Book.authors` ↔ `Author.books` 是雙向關聯。Jackson 序列化 Book 會展開 authors，再展開每個 author 的 books……**無限遞迴 / StackOverflow**。

### 問題 B：LAZY 關聯 → `LazyInitializationException`

`BookCopy.book`、`Book.authors` 都是 LAZY。當 Controller 回傳 entity、序列化發生在**交易結束之後**，存取未初始化的關聯就會丟 `LazyInitializationException`。

### 解法：DTO（record）+ 在交易內完成映射

- 用不可變的 `record` 當回應物件，只暴露需要的欄位，切斷雙向關聯。
- 映射在 `@Transactional` 的 service 內完成，此時關聯還能安全載入。

```java
public record BookDto(Long id, String isbn, String title, String description,
                      LocalDate publishDate, List<AuthorDto> authors, long copyCount) {
    public static BookDto from(Book book, long copyCount) { ... }
}
```

- 取明細時用 fetch join 一次撈齊作者，避免 N+1：

```java
@Query("select b from Book b left join fetch b.authors where b.id = :id")
Optional<Book> findByIdWithAuthors(@Param("id") Long id);
```

### 問題 C：直接回傳 `Page<T>` 會有警告

Spring Boot 3 序列化 `PageImpl` 會發出「結構不穩定」警告。

**解法**：自訂 `PageResponse<T>` record 包裝，格式穩定可控。

```java
public record PageResponse<T>(List<T> content, int page, int size,
                              long totalElements, int totalPages) {
    public static <T> PageResponse<T> from(Page<T> page) { ... }
}
```

---

## 4. 驗證與統一例外處理

- 請求 DTO 加 jakarta validation（`@NotBlank` / `@NotNull`），Controller 用 `@Valid` 觸發（需 `spring-boot-starter-validation`）。
- `@RestControllerAdvice` 把例外轉成 RFC 9457 的 `ProblemDetail`，避免 stack trace 外洩。

| 例外 | HTTP | 說明 |
|------|------|------|
| `NotFoundException` | 404 | 找不到資源 |
| `MethodArgumentNotValidException` | 400 | `@Valid` 欄位驗證失敗 |
| `HttpMessageNotReadableException` | 400 | request body 無法解析（如非法 enum 值）|

> 第三項特別重要：非法 enum（如 `status:"NOPE"`）在進入 controller 前就在反序列化階段失敗，**不會**被 `MethodArgumentNotValidException` 接到，需單獨處理，否則會回傳含完整 stack trace 的預設錯誤頁。

---

## 5. 環境注意事項：JDK 17 與 Spring Boot 版本

- 原專案是 Spring Boot **1.5.6**（Java 8 時代），其內含的舊版 cglib 在 **JDK 9+ 模組系統**下會丟 `InaccessibleObjectException`，無法啟動。
- 解法：升級到 Spring Boot **3.3.5**（最低需 Java 17），契合本機的 JDK 17。
- 連帶影響：`javax.*` → `jakarta.*` 命名空間（本專案重建時直接用 jakarta）、測試從 JUnit 4 改 JUnit 5。

---

## 附錄：本機啟動

```bash
docker compose up -d        # 起 PostgreSQL（含 healthcheck）
./mvnw spring-boot:run      # 啟動 app，首次啟動自動種入 17k 筆資料

# 關閉
docker compose down         # 停容器（保留資料）
docker compose down -v      # 連資料 volume 一起清掉
```

種子資料量可由 `application.properties` 調整：

```properties
library.seed.enabled=true
library.seed.authors=1000
library.seed.books=4000
library.seed.batch-size=100
```
