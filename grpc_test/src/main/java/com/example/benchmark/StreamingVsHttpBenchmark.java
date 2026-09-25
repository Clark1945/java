package com.example.benchmark;

import com.example.grpc.server.streaming.OrderServiceImpl;
import com.example.grpc.server.streaming.SensorServiceImpl;
import com.example.grpc.streaming.ListOrdersRequest;
import com.example.grpc.streaming.OrderResponseBatch;
import com.example.grpc.streaming.OrderServiceGrpc;
import com.example.grpc.streaming.ReportSummary;
import com.example.grpc.streaming.SensorReading;
import com.example.grpc.streaming.SensorReadingBatch;
import com.example.grpc.streaming.SensorServiceGrpc;
import com.example.http.client.HttpBenchmarkClient;
import com.example.http.model.SensorReadingDto;
import com.example.http.server.HttpBenchmarkServer;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 拿同樣筆數的資料，比較 gRPC batched streaming 跟傳統 REST（單一大 request/response）的耗時「跟」資料量大小。
 * 逐筆送一個 record 一個 message 的 naive 寫法在大資料量下不是真實世界會出現的用法（本機測試甚至會把
 * Netty 的 direct memory buffer 灌爆），所以這裡只保留有實際意義的「批次 streaming vs 單一大包」對照。
 * - Client streaming vs HTTP 大 POST：client 把 N 筆資料分批送給 server 彙總。
 * - Server streaming vs HTTP 大 GET ：server 把 N 筆資料分批吐回給 client。
 */
public class StreamingVsHttpBenchmark {

    private static final int GRPC_PORT = 9091;
    private static final int HTTP_PORT = 8081;
    private static final int BATCH_SIZE = 1000;
    private static final int[] SIZES = {1_000, 10_000, 100_000, 500_000, 1_000_000};

    /** elapsedMs：耗時；bytes：實際在網路上跑的位元組數（gRPC 是 protobuf 序列化大小，HTTP 是 JSON body 大小）。 */
    private record BenchResult(long elapsedMs, long bytes) {
        double megabytes() {
            return bytes / 1024.0 / 1024.0;
        }
    }

    public static void main(String[] args) throws Exception {
        Server grpcServer = ServerBuilder.forPort(GRPC_PORT)
                .addService(new SensorServiceImpl())
                .addService(new OrderServiceImpl())
                .build()
                .start();

        HttpBenchmarkServer httpServer = new HttpBenchmarkServer(HTTP_PORT);
        httpServer.start();

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", GRPC_PORT)
                .usePlaintext()
                .build();
        HttpBenchmarkClient httpClient = new HttpBenchmarkClient("http://localhost:" + HTTP_PORT);

        try {
            // warm-up：先跑一輪小資料量，讓 JIT / connection pool 都熱開，避免第一筆數字失真
            runClientStreamingGrpcBatched(channel, 500);
            httpClient.sendSensorReadings(buildReadings(500));
            runServerStreamingGrpcBatched(channel, 500);
            httpClient.listOrders("warmup", 500);

            String header = String.format("%-10s %-12s %-12s %-12s %-12s",
                    "records", "gRPC(ms)", "gRPC(MB)", "HTTP(ms)", "HTTP(MB)");

            System.out.println();
            System.out.println("== Client streaming upload: batched gRPC vs single large POST ==");
            System.out.println(header);
            for (int size : SIZES) {
                BenchResult grpcResult = runClientStreamingGrpcBatched(channel, size);
                BenchResult httpResult = runClientStreamingHttp(httpClient, size);
                printRow(size, grpcResult, httpResult);
            }

            System.out.println();
            System.out.println("== Server streaming download: batched gRPC vs single large GET ==");
            System.out.println(header);
            for (int size : SIZES) {
                BenchResult grpcResult = runServerStreamingGrpcBatched(channel, size);
                BenchResult httpResult = runServerStreamingHttp(httpClient, size);
                printRow(size, grpcResult, httpResult);
            }
        } finally {
            channel.shutdown();
            httpServer.stop();
            grpcServer.shutdown();
        }
    }

    private static void printRow(int size, BenchResult grpcResult, BenchResult httpResult) {
        System.out.printf("%-10d %-12d %-12.2f %-12d %-12.2f%n",
                size, grpcResult.elapsedMs(), grpcResult.megabytes(),
                httpResult.elapsedMs(), httpResult.megabytes());
    }

    // ---------- Client streaming（上傳） ----------

    private static BenchResult runClientStreamingGrpcBatched(ManagedChannel channel, int count) throws InterruptedException {
        SensorServiceGrpc.SensorServiceStub stub = SensorServiceGrpc.newStub(channel);
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<ReportSummary> resultRef = new AtomicReference<>();

        long start = System.nanoTime();

        StreamObserver<SensorReadingBatch> requestObserver = stub.reportReadingsBatched(new StreamObserver<>() {
            @Override
            public void onNext(ReportSummary summary) {
                resultRef.set(summary);
            }

            @Override
            public void onError(Throwable t) {
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                latch.countDown();
            }
        });

        long totalBytes = 0;
        SensorReadingBatch.Builder batch = SensorReadingBatch.newBuilder();
        for (int i = 0; i < count; i++) {
            batch.addReadings(buildSensorReading(i));

            if (batch.getReadingsCount() >= BATCH_SIZE) {
                SensorReadingBatch built = batch.build();
                totalBytes += built.getSerializedSize();
                requestObserver.onNext(built);
                batch = SensorReadingBatch.newBuilder();
            }
        }
        if (batch.getReadingsCount() > 0) {
            SensorReadingBatch built = batch.build();
            totalBytes += built.getSerializedSize();
            requestObserver.onNext(built);
        }
        requestObserver.onCompleted();

        latch.await(120, TimeUnit.SECONDS);
        long elapsedMs = (System.nanoTime() - start) / 1_000_000;

        ReportSummary summary = resultRef.get();
        if (summary == null || summary.getCount() != count) {
            throw new IllegalStateException("gRPC batched 回傳筆數不對: " + summary);
        }
        return new BenchResult(elapsedMs, totalBytes);
    }

    private static BenchResult runClientStreamingHttp(HttpBenchmarkClient client, int count) throws Exception {
        List<SensorReadingDto> readings = buildReadings(count);

        long start = System.nanoTime();
        HttpBenchmarkClient.UploadResult result = client.sendSensorReadings(readings);
        long elapsedMs = (System.nanoTime() - start) / 1_000_000;

        if (result.summary().getCount() != count) {
            throw new IllegalStateException("HTTP 回傳筆數不對: " + result.summary().getCount());
        }
        return new BenchResult(elapsedMs, result.requestBytes());
    }

    private static List<SensorReadingDto> buildReadings(int count) {
        List<SensorReadingDto> readings = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            readings.add(new SensorReadingDto(
                    "device-" + (i % 100),
                    i,
                    1_700_000_000_000L + i * 1000L,
                    "C",
                    "warehouse-" + (i % 50),
                    50 + (i % 50) * 0.1,
                    i % 20 == 0 ? "WARN" : "OK",
                    "v1.4.2"));
        }
        return readings;
    }

    private static SensorReading buildSensorReading(int i) {
        return SensorReading.newBuilder()
                .setDeviceId("device-" + (i % 100))
                .setValue(i)
                .setTimestamp(1_700_000_000_000L + i * 1000L)
                .setUnit("C")
                .setLocation("warehouse-" + (i % 50))
                .setBatteryLevel(50 + (i % 50) * 0.1)
                .setStatus(i % 20 == 0 ? "WARN" : "OK")
                .setFirmwareVersion("v1.4.2")
                .build();
    }

    // ---------- Server streaming（下載） ----------

    private static BenchResult runServerStreamingGrpcBatched(ManagedChannel channel, int count) {
        OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);

        long start = System.nanoTime();
        Iterator<OrderResponseBatch> it = stub.listOrdersBatched(ListOrdersRequest.newBuilder()
                .setUserId("bench")
                .setLimit(count)
                .setBatchSize(BATCH_SIZE)
                .build());

        int received = 0;
        long totalBytes = 0;
        while (it.hasNext()) {
            OrderResponseBatch batch = it.next();
            received += batch.getOrdersCount();
            totalBytes += batch.getSerializedSize();
        }
        long elapsedMs = (System.nanoTime() - start) / 1_000_000;

        if (received != count) {
            throw new IllegalStateException("gRPC batched 收到筆數不對: " + received);
        }
        return new BenchResult(elapsedMs, totalBytes);
    }

    private static BenchResult runServerStreamingHttp(HttpBenchmarkClient client, int count) throws Exception {
        long start = System.nanoTime();
        HttpBenchmarkClient.DownloadResult result = client.listOrders("bench", count);
        long elapsedMs = (System.nanoTime() - start) / 1_000_000;

        if (result.orders().size() != count) {
            throw new IllegalStateException("HTTP 收到筆數不對: " + result.orders().size());
        }
        return new BenchResult(elapsedMs, result.responseBytes());
    }
}
