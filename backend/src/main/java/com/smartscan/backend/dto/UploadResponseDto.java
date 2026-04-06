package com.smartscan.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadResponseDto {

    private Long id;
    private String studentName;
    private String fileName;
    private String fileType;
    private String filePath;
    private String status;
    private LocalDateTime uploadTime;
    private String modelName;
    private Integer score;
    private Integer llmScore;
    private Double similarity;
    private String feedback;
    private Long questionId;
}