package com.smartscan.backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessingStatusResponseDto {

    private Long answerSheetId;
    private String fileName;
    private String status;
}