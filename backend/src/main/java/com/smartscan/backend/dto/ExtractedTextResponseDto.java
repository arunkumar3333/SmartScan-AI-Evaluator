package com.smartscan.backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExtractedTextResponseDto {

    private Long answerSheetId;
    private String extractedText;
}