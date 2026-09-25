package com.example.grpc.server.streaming;

import com.example.grpc.streaming.ReportSummary;
import com.example.grpc.streaming.SensorReading;
import com.example.grpc.streaming.SensorReadingBatch;
import com.example.grpc.streaming.SensorServiceGrpc;
import io.grpc.stub.StreamObserver;

/** Client streaming 範例：陸續收 IoT 讀數，收完才回一次彙總結果。 */
public class SensorServiceImpl extends SensorServiceGrpc.SensorServiceImplBase {

    @Override
    public StreamObserver<SensorReading> reportReadings(StreamObserver<ReportSummary> responseObserver) {
        return new StreamObserver<>() {
            private int count = 0;
            private double sum = 0;

            @Override
            public void onNext(SensorReading reading) {
                count++;
                sum += reading.getValue();
            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(ReportSummary.newBuilder()
                        .setCount(count)
                        .setAverage(count == 0 ? 0 : sum / count)
                        .build());
                responseObserver.onCompleted();
            }
        };
    }

    /** 跟 reportReadings 一樣的邏輯，差別只在 client 端把多筆讀數打包成一個 SensorReadingBatch 再送。 */
    @Override
    public StreamObserver<SensorReadingBatch> reportReadingsBatched(StreamObserver<ReportSummary> responseObserver) {
        return new StreamObserver<>() {
            private int count = 0;
            private double sum = 0;

            @Override
            public void onNext(SensorReadingBatch batch) {
                for (SensorReading reading : batch.getReadingsList()) {
                    count++;
                    sum += reading.getValue();
                }
            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(ReportSummary.newBuilder()
                        .setCount(count)
                        .setAverage(count == 0 ? 0 : sum / count)
                        .build());
                responseObserver.onCompleted();
            }
        };
    }
}
