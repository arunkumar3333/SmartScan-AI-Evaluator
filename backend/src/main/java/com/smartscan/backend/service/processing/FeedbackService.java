package com.smartscan.backend.service.processing;

import org.springframework.stereotype.Service;

@Service
public class FeedbackService {

    public String generate(String answer, double similarity) {

        if (answer.length() < 20) {
            return "Answer is too short. Add more explanation.";
        }

        if (similarity > 0.75) {
            return "Excellent answer with strong understanding.";
        } else if (similarity > 0.5) {
            return "Good answer but missing some key points.";
        } else if (similarity > 0.3) {
            return "Basic understanding, needs improvement.";
        } else {
            return "Incorrect or irrelevant answer.";
        }
    }
}