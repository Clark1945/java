package com.example.grpc.server.streaming;

import com.example.grpc.streaming.LocationServiceGrpc;
import com.example.grpc.streaming.LocationUpdate;
import com.example.grpc.streaming.NearbyUsers;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/** Bidirectional streaming 範例：client 持續回報座標，server 持續回傳附近的人。 */
public class LocationServiceImpl extends LocationServiceGrpc.LocationServiceImplBase {

    private static final double NEARBY_THRESHOLD = 0.05;

    private final Map<String, double[]> lastKnownLocation = new ConcurrentHashMap<>();

    @Override
    public StreamObserver<LocationUpdate> streamLocation(StreamObserver<NearbyUsers> responseObserver) {
        return new StreamObserver<>() {
            @Override
            public void onNext(LocationUpdate update) {
                lastKnownLocation.put(update.getUserId(), new double[]{update.getLat(), update.getLng()});

                List<String> nearby = lastKnownLocation.entrySet().stream()
                        .filter(entry -> !entry.getKey().equals(update.getUserId()))
                        .filter(entry -> distance(entry.getValue(), update.getLat(), update.getLng()) <= NEARBY_THRESHOLD)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());

                responseObserver.onNext(NearbyUsers.newBuilder().addAllUserIds(nearby).build());
            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onCompleted();
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }

    private static double distance(double[] point, double lat, double lng) {
        double dLat = point[0] - lat;
        double dLng = point[1] - lng;
        return Math.sqrt(dLat * dLat + dLng * dLng);
    }
}
