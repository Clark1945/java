# java 練習

Java 相關的練習與示範專案集合，每個子目錄都是一個獨立可建置的專案。

## 專案列表

| 目錄 | 說明 |
|---|---|
| [java_test](java_test) | Java 語言特性與 Design Pattern 練習（Optional、Chain of Responsibility 等） |
| [spring_test](spring_test) | Spring Boot 3.3.5 + Java 17 + PostgreSQL 的圖書館範例 API（Book / Author / BookCopy），[docs/DESIGN.md](spring_test/docs/DESIGN.md) 記錄了 EntityManager 批次寫入、序列化避雷等設計筆記 |
| [todo-notification](todo-notification) | 串接 Google Tasks + LINE Bot 的待辦提醒服務，會自動產生待辦清單圖片並推播 |
| [e-commerce-microservice](e-commerce-microservice) | 微服務架構的電商網站範例（原獨立 repo 併入，保留原始 commit 歷史） |
| [the-ticket-system](the-ticket-system) | 模擬正式環境等級的票務與金流系統（原獨立 repo 併入，保留原始 commit 歷史） |
| grpc_test | gRPC Java 專案骨架，涵蓋 unary / client-streaming / server-streaming / bidi streaming 範例，並實測 gRPC batched streaming vs 傳統 REST 在大資料量下的效能與傳輸量差異，詳見 [PR #2](https://github.com/Clark1945/java/pull/2)（尚未合併） |

## 備註

- `e-commerce-microservice`、`the-ticket-system` 是從各自獨立的 GitHub repo 用 `git subtree` 併入，原本的 commit 歷史都保留在這裡；原本的獨立 repo 目前仍保留，尚未刪除。
- 各子專案的建置方式請參考各自目錄下的 `pom.xml` / `README`。
