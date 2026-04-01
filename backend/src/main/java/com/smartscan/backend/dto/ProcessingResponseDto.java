package com.smartscan.backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessingResponseDto {

    private Long id;
    private String fileName;
    private String status;
    private Integer score;
}