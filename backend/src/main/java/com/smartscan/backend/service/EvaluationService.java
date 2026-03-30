package com.smartscan.backend.service;

import org.springframework.stereotype.Service;
import java.util.*;

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

            if (answers.get(i).contains(correct)) {
                score++;
            }
        }

        return score;
    }
}