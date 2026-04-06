package com.smartscan.backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluationResultResponseDto {

    private Long answerSheetId;
    private String fileName;
    private Integer score;
    private String status;
    private Double similarity;
    private String feedback;
    private Integer llmScore;
}