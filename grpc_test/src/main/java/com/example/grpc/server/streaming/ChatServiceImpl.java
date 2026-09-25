package com.example.grpc.server.streaming;

import com.example.grpc.streaming.ChatMessage;
import com.example.grpc.streaming.ChatServiceGrpc;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/** Bidirectional streaming 範例：簡易聊天室，收到訊息就廣播給所有連線中的 client。 */
public class ChatServiceImpl extends ChatServiceGrpc.ChatServiceImplBase {

    private final List<StreamObserver<ChatMessage>> room = new CopyOnWriteArrayList<>();

    @Override
    public StreamObserver<ChatMessage> chat(StreamObserver<ChatMessage> responseObserver) {
        room.add(responseObserver);

        return new StreamObserver<>() {
            @Override
            public void onNext(ChatMessage message) {
                for (StreamObserver<ChatMessage> member : room) {
                    member.onNext(message);
                }
            }

            @Override
            public void onError(Throwable t) {
                room.remove(responseObserver);
            }

            @Override
            public void onCompleted() {
                room.remove(responseObserver);
                responseObserver.onCompleted();
            }
        };
    }
}
