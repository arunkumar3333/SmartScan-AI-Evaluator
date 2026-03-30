package com.smartscan.backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class ProcessingResponse {
    private List<String> answers;
    private int score;
}