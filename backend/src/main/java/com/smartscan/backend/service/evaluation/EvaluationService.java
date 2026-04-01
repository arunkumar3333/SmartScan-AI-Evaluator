package com.smartscan.backend.service.evaluation;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EvaluationService {

    private static final Map<Integer, String> KEY = new HashMap<>();

    static {
        KEY.put(1, "A");
        KEY.put(2, "B");
        KEY.put(3, "C");
    }

    public int evaluate(List<String> answers) {
        int score = 0;

        for (int i = 0; i < answers.size(); i++) {
            String correct = KEY.get(i + 1);

            if (correct != null && answers.get(i).toUpperCase().contains(correct)) {
                score++;
            }
        }

        return score;
    }
}