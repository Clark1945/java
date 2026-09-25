package com.example.http.client;

import com.example.http.model.OrderDto;
import com.example.http.model.ReportSummaryDto;
import com.example.http.model.SensorReadingDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class HttpBenchmarkClient {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String baseUrl;

    public HttpBenchmarkClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /** requestBytes：送出去的 JSON request body 大小；用來跟 gRPC 的序列化大小對照。 */
    public record UploadResult(ReportSummaryDto summary, long requestBytes) {
    }

    /** responseBytes：收到的 JSON response body 大小；用來跟 gRPC 的序列化大小對照。 */
    public record DownloadResult(List<OrderDto> orders, long responseBytes) {
    }

    /** 對應 gRPC client streaming：一次把整包 JSON 陣列送出去，等 server 回傳彙總結果。 */
    public UploadResult sendSensorReadings(List<SensorReadingDto> readings) throws Exception {
        byte[] body = mapper.writeValueAsBytes(readings);

        HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl + "/sensor-readings"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofByteArray(body))
                .build();

        HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
        ReportSummaryDto summary = mapper.readValue(response.body(), ReportSummaryDto.class);
        return new UploadResult(summary, body.length);
    }

    /** 對應 gRPC server streaming：一次等 server 把整包 JSON 陣列組好回傳，才能開始解析。 */
    public DownloadResult listOrders(String userId, int limit) throws Exception {
        URI uri = URI.create(baseUrl + "/orders?userId=" + userId + "&limit=" + limit);

        HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
        HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());

        List<OrderDto> orders = mapper.readValue(response.body(),
                mapper.getTypeFactory().constructCollectionType(List.class, OrderDto.class));
        return new DownloadResult(orders, response.body().length);
    }
}
