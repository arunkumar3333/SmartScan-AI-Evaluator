package com.smartscan.backend.dto;

import lombok.Data;

@Data
public class QuestionRequestDto {
    private String questionText;
    private String modelAnswer;
    private Long teacherId;
}