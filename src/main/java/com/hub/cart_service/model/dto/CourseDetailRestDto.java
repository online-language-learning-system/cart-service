package com.hub.cart_service.model.dto;

public record CourseDetailRestDto (
        long courseId,
        String courseName,
        String instructor,
        float price,
        String imageUrl
) { }
