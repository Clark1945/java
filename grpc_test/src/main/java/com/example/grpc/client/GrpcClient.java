package com.example.grpc.client;

import com.example.grpc.wallet.BalanceRequest;
import com.example.grpc.wallet.BalanceResponse;
import com.example.grpc.wallet.WalletServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GrpcClient {

    public static void main(String[] args) throws Exception {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        try {
            WalletServiceGrpc.WalletServiceBlockingStub stub = WalletServiceGrpc.newBlockingStub(channel);

            BalanceRequest request = BalanceRequest.newBuilder()
                    .setUserId("u123")
                    .build();

            BalanceResponse response = stub.getBalance(request);
            System.out.println("balance = " + response.getBalance() + " " + response.getCurrency());
        } finally {
            channel.shutdown();
        }
    }
}
