package com.example.grpc.server.streaming;

import com.example.grpc.streaming.PriceUpdate;
import com.example.grpc.streaming.StockServiceGrpc;
import com.example.grpc.streaming.SubscribeRequest;
import io.grpc.stub.StreamObserver;

import java.util.Random;

/** Server streaming 範例：模擬股價持續推播，送幾筆後結束。 */
public class StockServiceImpl extends StockServiceGrpc.StockServiceImplBase {

    private static final int TICKS = 5;
    private final Random random = new Random();

    @Override
    public void subscribePrice(SubscribeRequest request, StreamObserver<PriceUpdate> responseObserver) {
        double price = 100.0;
        try {
            for (int i = 0; i < TICKS; i++) {
                price += (random.nextDouble() - 0.5) * 2;
                responseObserver.onNext(PriceUpdate.newBuilder()
                        .setSymbol(request.getSymbol())
                        .setPrice(price)
                        .build());
                Thread.sleep(200);
            }
            responseObserver.onCompleted();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            responseObserver.onError(e);
        }
    }
}
