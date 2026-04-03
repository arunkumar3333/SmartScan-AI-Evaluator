package com.smartscan.backend.service.processing;

import com.smartscan.backend.dto.GradingResult;
import com.smartscan.backend.service.ai.LangChainAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GraderService {

    private final SimilarityService similarityService;
    private final FeedbackService feedbackService;
    private final LangChainAiService aiService;

    public GradingResult evaluate(String student, String model) {

        double similarity = similarityService.calculate(student, model);

        int score = (int) Math.round(similarity * 10);

        String aiFeedback = aiService.evaluate(student, model);

        return new GradingResult(
                similarity,
                score,
                aiFeedback,
                score >= 5 ? "Pass" : "Fail"
        );
    }
}