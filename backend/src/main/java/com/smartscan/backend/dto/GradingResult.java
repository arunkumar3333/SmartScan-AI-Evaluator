package com.smartscan.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GradingResult {

    private double similarity;
    private int score;
    private String feedback;
}