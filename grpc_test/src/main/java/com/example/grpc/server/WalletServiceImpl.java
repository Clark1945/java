package com.example.grpc.server;

import com.example.grpc.wallet.BalanceRequest;
import com.example.grpc.wallet.BalanceResponse;
import com.example.grpc.wallet.WalletServiceGrpc;
import io.grpc.stub.StreamObserver;

public class WalletServiceImpl extends WalletServiceGrpc.WalletServiceImplBase {

    @Override
    public void getBalance(BalanceRequest request, StreamObserver<BalanceResponse> responseObserver) {
        BalanceResponse response = BalanceResponse.newBuilder()
                .setBalance(3000L)
                .setCurrency("TWD")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
