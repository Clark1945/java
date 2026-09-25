package com.example.grpc.server;

import com.example.grpc.server.streaming.ChatServiceImpl;
import com.example.grpc.server.streaming.FileUploadServiceImpl;
import com.example.grpc.server.streaming.LocationServiceImpl;
import com.example.grpc.server.streaming.OrderServiceImpl;
import com.example.grpc.server.streaming.SensorServiceImpl;
import com.example.grpc.server.streaming.StockServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;

public class GrpcServer {

    private static final int PORT = 9090;

    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(PORT)
                .addService(new WalletServiceImpl())
                .addService(new OrderServiceImpl())
                .addService(new StockServiceImpl())
                .addService(new FileUploadServiceImpl())
                .addService(new SensorServiceImpl())
                .addService(new ChatServiceImpl())
                .addService(new LocationServiceImpl())
                .build();

        server.start();
        System.out.println("gRPC server started, listening on port " + PORT);

        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));
        server.awaitTermination();
    }
}
