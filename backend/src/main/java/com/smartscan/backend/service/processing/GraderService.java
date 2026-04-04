package com.smartscan.backend.service.processing;

import com.smartscan.backend.dto.GradingResult;
import com.smartscan.backend.service.ai.OllamaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GraderService {

    private final SimilarityService similarityService;
    private final OllamaService ollamaService;

    public GradingResult evaluate(String studentAnswer, String modelAnswer) {

        // ✅ 1. Similarity
        double similarity = similarityService.cosineSimilarity(studentAnswer, modelAnswer);
        int similarityScore = (int) Math.round(similarity * 10);

        // ✅ 2. LLM feedback (this now returns structured response)
        var aiResponse = ollamaService.generateFeedback(studentAnswer, modelAnswer);

        int llmScore = (int) aiResponse.get("llmScore");

        // ✅ 3. Final score
        int finalScore = (llmScore + similarityScore) / 2;

        String feedback = (String) aiResponse.get("feedback");

        return new GradingResult(similarity, finalScore, feedback);
    }
}