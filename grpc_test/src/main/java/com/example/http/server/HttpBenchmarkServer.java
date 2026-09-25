package com.example.http.server;

import com.example.http.model.OrderDto;
import com.example.http.model.ReportSummaryDto;
import com.example.http.model.SensorReadingDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * 傳統 REST 對照組：
 * - POST /sensor-readings：對應 gRPC client streaming，一次收整包 JSON 陣列，算完再回傳彙總結果。
 * - GET  /orders         ：對應 gRPC server streaming，一次把整包 JSON 陣列組好才回傳。
 */
public class HttpBenchmarkServer {

    private final HttpServer server;
    private final ObjectMapper mapper = new ObjectMapper();

    public HttpBenchmarkServer(int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/sensor-readings", this::handleSensorReadings);
        server.createContext("/orders", this::handleListOrders);
        server.setExecutor(Executors.newCachedThreadPool());
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(0);
    }

    private void handleSensorReadings(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        List<SensorReadingDto> readings = mapper.readValue(
                exchange.getRequestBody(),
                mapper.getTypeFactory().constructCollectionType(List.class, SensorReadingDto.class));

        int count = readings.size();
        double sum = 0;
        for (SensorReadingDto reading : readings) {
            sum += reading.getValue();
        }

        ReportSummaryDto summary = new ReportSummaryDto(count, count == 0 ? 0 : sum / count);
        writeJson(exchange, summary);
    }

    private void handleListOrders(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        Map<String, String> query = parseQuery(exchange.getRequestURI());
        String userId = query.getOrDefault("userId", "u123");
        int limit = Integer.parseInt(query.getOrDefault("limit", "5"));

        List<OrderDto> orders = new ArrayList<>(limit);
        for (int i = 1; i <= limit; i++) {
            orders.add(new OrderDto(
                    userId + "-order-" + i,
                    i * 1000L,
                    "TWD",
                    i % 5 == 0 ? "REFUNDED" : "PAID",
                    1_700_000_000_000L + i * 1000L,
                    "customer-" + (i % 1000),
                    (i % 10) + 1,
                    "No. " + i + ", Sec. 1, Xinyi Rd, Taipei City, Taiwan"));
        }

        writeJson(exchange, orders);
    }

    private void writeJson(HttpExchange exchange, Object body) throws IOException {
        byte[] responseBytes = mapper.writeValueAsBytes(body);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }

    private static Map<String, String> parseQuery(URI uri) {
        Map<String, String> params = new java.util.HashMap<>();
        String query = uri.getRawQuery();
        if (query == null || query.isEmpty()) {
            return params;
        }
        for (String pair : query.split("&")) {
            int idx = pair.indexOf('=');
            if (idx > 0) {
                params.put(pair.substring(0, idx), pair.substring(idx + 1));
            }
        }
        return params;
    }
}
