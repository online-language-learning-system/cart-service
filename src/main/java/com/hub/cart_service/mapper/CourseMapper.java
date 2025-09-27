package com.hub.cart_service.mapper;

import com.hub.cart_service.grpc.CourseDetail;
import com.hub.cart_service.model.dto.CourseDetailRestDto;

public class CourseMapper {
    public static CourseDetailRestDto toRestDto(CourseDetail proto) {
        return new CourseDetailRestDto(
                proto.getCourseId(),
                proto.getCourseName(),
                proto.getInstructor(),
                proto.getPrice(),
                proto.getImageUrl()
        );
    }
}
