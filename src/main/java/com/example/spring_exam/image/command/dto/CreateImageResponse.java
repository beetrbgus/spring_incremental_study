package com.example.spring_exam.image.command.dto;

public record CreateImageResponse(
        Long imageId,
        String url
) {}