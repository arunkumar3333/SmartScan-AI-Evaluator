package com.smartscan.backend.service.ai;

import org.springframework.stereotype.Service;

@Service
public class LangChainAiService {

    public String evaluate(String studentAnswer, String modelAnswer) {

        String prompt = """
                You are an intelligent exam evaluator.

                Model Answer:
                %s

                Student Answer:
                %s

                Evaluate:
                - Accuracy
                - Completeness
                - Clarity

                Provide score out of 10 and feedback.
                """.formatted(modelAnswer, studentAnswer);

        return "Score: 7/10\nFeedback: Good answer but needs more detail.";
    }
}