package com.smartscan.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GradingResult {

    private double similarity;
    private int score;
    private String feedback;
    private String status;
}