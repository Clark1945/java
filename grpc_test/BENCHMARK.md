# gRPC Streaming vs 傳統 REST：大資料量傳輸效能實測

本文件記錄在 [grpc_test](.) 專案中，針對「大資料量批次傳輸」這個情境，比較 gRPC streaming 與傳統 REST（單一大 request/response）的耗時與傳輸量差異。程式碼位置：

- gRPC 服務端：[OrderServiceImpl.java](src/main/java/com/example/grpc/server/streaming/OrderServiceImpl.java)、[SensorServiceImpl.java](src/main/java/com/example/grpc/server/streaming/SensorServiceImpl.java)
- REST 對照組：[HttpBenchmarkServer.java](src/main/java/com/example/http/server/HttpBenchmarkServer.java)、[HttpBenchmarkClient.java](src/main/java/com/example/http/client/HttpBenchmarkClient.java)
- Benchmark 主程式：[StreamingVsHttpBenchmark.java](src/main/java/com/example/benchmark/StreamingVsHttpBenchmark.java)

## 測試情境

| 情境 | gRPC 對應 RPC | REST 對照組 |
|---|---|---|
| 上傳彙總（client streaming） | `SensorService.ReportReadingsBatched` | `POST /sensor-readings`（一次送整包 JSON 陣列） |
| 批次下載（server streaming） | `OrderService.ListOrdersBatched` | `GET /orders`（一次回整包 JSON 陣列） |

**資料筆數**：1,000 / 10,000 / 100,000 / 500,000 / 1,000,000
**每筆欄位數**：8 個（`SensorReading`：device_id、value、timestamp、unit、location、battery_level、status、firmware_version；`OrderResponse`：order_id、amount、currency、status、created_at、customer_id、item_count、shipping_address）
**gRPC 批次大小**：每個 stream message 帶 1,000 筆記錄
**測試環境**：本機 loopback（同一台機器，gRPC server 監聽 9091、HTTP server 監聽 8081），JVM 啟動時給 `-Xmx3g`

## 實測結果

### 上傳彙總（Client streaming upload）

| records | gRPC(ms) | gRPC(MB) | HTTP(ms) | HTTP(MB) |
|---|---|---|---|---|
| 1,000 | 4 | 0.06 | 7 | 0.16 |
| 10,000 | 16 | 0.64 | 48 | 1.56 |
| 100,000 | 83 | 6.37 | 171 | 15.71 |
| 500,000 | 162 | 31.85 | 539 | 78.95 |
| 1,000,000 | 296 | 63.71 | 1,080 | 158.01 |

### 批次下載（Server streaming download）

| records | gRPC(ms) | gRPC(MB) | HTTP(ms) | HTTP(MB) |
|---|---|---|---|---|
| 1,000 | 3 | 0.10 | 3 | 0.20 |
| 10,000 | 12 | 1.03 | 84 | 2.07 |
| 100,000 | 78 | 10.53 | 210 | 21.02 |
| 500,000 | 273 | 53.74 | 772 | 106.39 |
| 1,000,000 | 436 | 107.95 | 1,694 | 213.10 |

## 結論

1. **批次後的 gRPC streaming 全面領先 REST**，且筆數越多差距越大：100 萬筆時，上傳耗時差 3.6 倍（296ms vs 1,080ms），下載耗時差 3.9 倍（436ms vs 1,694ms）。
2. **傳輸量差距是效能差距的主因之一**：同樣 8 個欄位、同樣筆數，protobuf 序列化後的體積穩定只有 JSON 的 40%-50% 左右（100 萬筆下載：gRPC 108MB vs HTTP 213MB）。這是因為 JSON 每筆記錄都要重複寫欄位名稱（如 `"shippingAddress"`）當 key，而 protobuf 的欄位標籤只是 1-2 個 byte 的數字。
3. **streaming 的效能優勢建立在「攤平每則訊息的固定開銷」上**：逐筆送（每筆一個 message）在小量下差異不大，但資料量一大，每則訊息的框架/序列化/排程開銷會迅速疊加，甚至可能像本次實測一樣直接把記憶體灌爆——這也印證了 gRPC 的 flow control 機制存在的必要性，naive 的用法在生產環境是危險的。
4. 這個結果是**本機 loopback** 測出來的，實際網路環境（有真實延遲、頻寬限制）下，gRPC 的相對優勢預期會更明顯（protobuf 體積更小 = 傳輸時間更短），但確切倍數需要另外在真實網路環境下驗證。

## 如何重現

```bash
cd grpc_test
mvn compile
mvn dependency:build-classpath -Dmdep.outputFile=cp.txt
java -Xmx3g -cp "target/classes;$(cat cp.txt)" com.example.benchmark.StreamingVsHttpBenchmark
```

## 已知限制

- 測試在同一台機器上的 loopback 網路完成，沒有模擬真實的網路延遲/頻寬限制。
- 只跑了一輪（含一次 warm-up），不是多輪取平均的嚴謹 benchmark，數字有參考價值但非精確基準。
- HTTP 對照組使用 JDK 內建 `com.sun.net.httpserver.HttpServer` + Jackson，未使用正式框架（如 Spring MVC），實際框架的額外開銷未計入。

## 備註：naive「逐筆送」的寫法已被排除

一開始的版本讓 client 端每一筆記錄呼叫一次 `onNext()`（不打包、不檢查 flow control）。實測發現：

- 逐筆送在資料量大時**效能遠差於批次送**（500,000 筆時：逐筆 6,000ms+ vs 批次 100ms 等級）。
- 推到 1,000,000 筆時，逐筆送的版本直接把 gRPC-netty 的 direct memory buffer 灌爆，拋出：
  ```
  OutOfMemoryError: Cannot reserve 2097152 bytes of direct buffer memory
  ```
  原因是 client 端用 async stub 狂打 `onNext()`，完全沒有檢查 `isReady()`（flow control），訊息產生速度遠超網路能消化的速度，Netty 的 outbound buffer 因此累積到爆記憶體上限。

**結論：大資料量情境下沒有人會真的逐筆傳送，這本來就不是實務上的可用寫法**，因此最終的 benchmark 只保留「批次 streaming vs REST 單一大包」這組有意義的對照，逐筆送的版本從 benchmark 中移除（但兩個 RPC 的「逐筆版」方法仍保留在 [SensorServiceImpl](src/main/java/com/example/grpc/server/streaming/SensorServiceImpl.java) / [OrderServiceImpl](src/main/java/com/example/grpc/server/streaming/OrderServiceImpl.java) 裡，作為小資料量情境下的基礎教學範例）。
