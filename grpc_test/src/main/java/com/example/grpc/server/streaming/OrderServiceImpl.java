package com.example.grpc.server.streaming;

import com.example.grpc.streaming.ListOrdersRequest;
import com.example.grpc.streaming.OrderResponse;
import com.example.grpc.streaming.OrderResponseBatch;
import com.example.grpc.streaming.OrderServiceGrpc;
import io.grpc.stub.StreamObserver;

/** Server streaming 範例：一次請求，分批把歷史訂單吐回去。 */
public class OrderServiceImpl extends OrderServiceGrpc.OrderServiceImplBase {

    private static final int DEFAULT_BATCH_SIZE = 1000;

    @Override
    public void listOrders(ListOrdersRequest request, StreamObserver<OrderResponse> responseObserver) {
        int limit = request.getLimit() > 0 ? request.getLimit() : 5;
        for (int i = 1; i <= limit; i++) {
            responseObserver.onNext(buildOrder(request.getUserId(), i));
        }
        responseObserver.onCompleted();
    }

    /** 跟 listOrders 一樣的邏輯，差別只在 server 端把多筆訂單打包成一個 OrderResponseBatch 再送，攤平每則訊息的固定開銷。 */
    @Override
    public void listOrdersBatched(ListOrdersRequest request, StreamObserver<OrderResponseBatch> responseObserver) {
        int limit = request.getLimit() > 0 ? request.getLimit() : 5;
        int batchSize = request.getBatchSize() > 0 ? request.getBatchSize() : DEFAULT_BATCH_SIZE;

        OrderResponseBatch.Builder batch = OrderResponseBatch.newBuilder();
        for (int i = 1; i <= limit; i++) {
            batch.addOrders(buildOrder(request.getUserId(), i));

            if (batch.getOrdersCount() >= batchSize) {
                responseObserver.onNext(batch.build());
                batch = OrderResponseBatch.newBuilder();
            }
        }
        if (batch.getOrdersCount() > 0) {
            responseObserver.onNext(batch.build());
        }
        responseObserver.onCompleted();
    }

    private static OrderResponse buildOrder(String userId, int i) {
        return OrderResponse.newBuilder()
                .setOrderId(userId + "-order-" + i)
                .setAmount(i * 1000L)
                .setCurrency("TWD")
                .setStatus(i % 5 == 0 ? "REFUNDED" : "PAID")
                .setCreatedAt(1_700_000_000_000L + i * 1000L)
                .setCustomerId("customer-" + (i % 1000))
                .setItemCount((i % 10) + 1)
                .setShippingAddress("No. " + i + ", Sec. 1, Xinyi Rd, Taipei City, Taiwan")
                .build();
    }
}
