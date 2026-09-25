package com.example.grpc.client;

import com.example.grpc.streaming.*;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/** 驗證用：依序打過六個 streaming 範例的 RPC，把結果印出來看是否符合預期。 */
public class StreamingClientDemo {

    public static void main(String[] args) throws Exception {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        try {
            serverStreamingOrders(channel);
//            serverStreamingStock(channel);
//            clientStreamingUpload(channel);
//            clientStreamingSensor(channel);
//            bidiStreamingChat(channel);
//            bidiStreamingLocation(channel);
        } finally {
            channel.shutdown();
        }
    }

    private static void serverStreamingOrders(ManagedChannel channel) {
        System.out.println("== [1] Server streaming: ListOrders ==");
        OrderServiceGrpc.OrderServiceBlockingStub stub = OrderServiceGrpc.newBlockingStub(channel);
        Iterator<OrderResponse> it = stub.listOrders(ListOrdersRequest.newBuilder().setUserId("u123").build());
        while (it.hasNext()) {
            OrderResponse order = it.next();
            System.out.println("  order=" + order.getOrderId() + " amount=" + order.getAmount());
        }
    }

    private static void serverStreamingStock(ManagedChannel channel) {
        System.out.println("== [2] Server streaming: SubscribePrice ==");
        StockServiceGrpc.StockServiceBlockingStub stub = StockServiceGrpc.newBlockingStub(channel);
        Iterator<PriceUpdate> it = stub.subscribePrice(SubscribeRequest.newBuilder().setSymbol("TSMC").build());
        while (it.hasNext()) {
            PriceUpdate update = it.next();
            System.out.println("  symbol=" + update.getSymbol() + " price=" + update.getPrice());
        }
    }

    private static void clientStreamingUpload(ManagedChannel channel) throws InterruptedException {
        System.out.println("== [3] Client streaming: UploadFile ==");
        FileUploadServiceGrpc.FileUploadServiceStub stub = FileUploadServiceGrpc.newStub(channel);
        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<FileChunk> requestObserver = stub.uploadFile(new StreamObserver<>() {
            @Override
            public void onNext(UploadResult result) {
                System.out.println("  fileId=" + result.getFileId() + " totalBytes=" + result.getTotalBytes());
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                latch.countDown();
            }
        });

        for (int i = 0; i < 3; i++) {
            requestObserver.onNext(FileChunk.newBuilder()
                    .setData(ByteString.copyFromUtf8("chunk-" + i))
                    .build());
        }
        requestObserver.onCompleted();

        latch.await(5, TimeUnit.SECONDS);
    }

    private static void clientStreamingSensor(ManagedChannel channel) throws InterruptedException {
        System.out.println("== [4] Client streaming: ReportReadings ==");
        SensorServiceGrpc.SensorServiceStub stub = SensorServiceGrpc.newStub(channel);
        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<SensorReading> requestObserver = stub.reportReadings(new StreamObserver<>() {
            @Override
            public void onNext(ReportSummary summary) {
                System.out.println("  count=" + summary.getCount() + " average=" + summary.getAverage());
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                latch.countDown();
            }
        });

        double[] values = {10.0, 20.0, 30.0};
        for (double value : values) {
            requestObserver.onNext(SensorReading.newBuilder()
                    .setDeviceId("device-1")
                    .setValue(value)
                    .build());
        }
        requestObserver.onCompleted();

        latch.await(5, TimeUnit.SECONDS);
    }

    private static void bidiStreamingChat(ManagedChannel channel) throws InterruptedException {
        System.out.println("== [5] Bidirectional streaming: Chat ==");
        ChatServiceGrpc.ChatServiceStub stub = ChatServiceGrpc.newStub(channel);
        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<ChatMessage> requestObserver = stub.chat(new StreamObserver<>() {
            int received = 0;

            @Override
            public void onNext(ChatMessage message) {
                System.out.println("  [" + message.getUser() + "] " + message.getText());
                received++;
                if (received >= 2) {
                    latch.countDown();
                }
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                latch.countDown();
            }
        });

        requestObserver.onNext(ChatMessage.newBuilder().setUser("clark").setText("hello").build());
        requestObserver.onNext(ChatMessage.newBuilder().setUser("clark").setText("gRPC streaming works").build());

        latch.await(5, TimeUnit.SECONDS);
        requestObserver.onCompleted();
    }

    private static void bidiStreamingLocation(ManagedChannel channel) throws InterruptedException {
        System.out.println("== [6] Bidirectional streaming: StreamLocation ==");
        LocationServiceGrpc.LocationServiceStub stub = LocationServiceGrpc.newStub(channel);
        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<LocationUpdate> requestObserver = stub.streamLocation(new StreamObserver<>() {
            int received = 0;

            @Override
            public void onNext(NearbyUsers nearbyUsers) {
                System.out.println("  nearby=" + nearbyUsers.getUserIdsList());
                received++;
                if (received >= 2) {
                    latch.countDown();
                }
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                latch.countDown();
            }
        });

        requestObserver.onNext(LocationUpdate.newBuilder().setUserId("alice").setLat(25.03).setLng(121.56).build());
        requestObserver.onNext(LocationUpdate.newBuilder().setUserId("bob").setLat(25.031).setLng(121.561).build());

        latch.await(5, TimeUnit.SECONDS);
        requestObserver.onCompleted();
    }
}
