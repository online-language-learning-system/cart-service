package com.hub.cart_service.grpcclient;

import com.hub.cart_service.grpc.CourseListRequest;
import com.hub.cart_service.grpc.CourseListResponse;
import com.hub.cart_service.grpc.CourseServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

@Component
public class CourseGrpcClient {

    private ManagedChannel channel;
    private CourseServiceGrpc.CourseServiceBlockingStub stub;

    @PostConstruct
    public void init() {
        channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();
        stub = CourseServiceGrpc.newBlockingStub(channel);
    }

    public CourseListResponse getCourseDetails(List<Long> courseIds) {
        CourseListRequest request = CourseListRequest.newBuilder()
                .addAllCourseId(courseIds)
                .build();
        return stub.getCourseDetails(request);
    }

    @PreDestroy
    public void shutdown() {
        if (channel != null) channel.shutdown();
    }
}
