package com.example.grpc.server.streaming;

import com.example.grpc.streaming.FileChunk;
import com.example.grpc.streaming.FileUploadServiceGrpc;
import com.example.grpc.streaming.UploadResult;
import io.grpc.stub.StreamObserver;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

/** Client streaming 範例：client 端邊讀邊送 chunk，server 收完才回一次結果。 */
public class FileUploadServiceImpl extends FileUploadServiceGrpc.FileUploadServiceImplBase {

    @Override
    public StreamObserver<FileChunk> uploadFile(StreamObserver<UploadResult> responseObserver) {
        return new StreamObserver<>() {
            private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            @Override
            public void onNext(FileChunk chunk) {
                byte[] data = chunk.getData().toByteArray();
                buffer.write(data, 0, data.length);
            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(UploadResult.newBuilder()
                        .setFileId(UUID.randomUUID().toString())
                        .setTotalBytes(buffer.size())
                        .build());
                responseObserver.onCompleted();
            }
        };
    }
}
