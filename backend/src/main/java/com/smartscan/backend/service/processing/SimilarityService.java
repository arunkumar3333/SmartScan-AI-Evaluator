package com.smartscan.backend.service.processing;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class SimilarityService {

    public double calculate(String text1, String text2) {

        Map<String, Integer> freq1 = getFreq(text1);
        Map<String, Integer> freq2 = getFreq(text2);

        Set<String> words = new HashSet<>();
        words.addAll(freq1.keySet());
        words.addAll(freq2.keySet());

        int dot = 0, norm1 = 0, norm2 = 0;

        for (String w : words) {
            int v1 = freq1.getOrDefault(w, 0);
            int v2 = freq2.getOrDefault(w, 0);

            dot += v1 * v2;
            norm1 += v1 * v1;
            norm2 += v2 * v2;
        }

        if (norm1 == 0 || norm2 == 0) return 0;

        return dot / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    private Map<String, Integer> getFreq(String text) {
        Map<String, Integer> map = new HashMap<>();

        for (String w : text.toLowerCase().split("\\W+")) {
            if (!w.isEmpty()) {
                map.put(w, map.getOrDefault(w, 0) + 1);
            }
        }
        return map;
    }
}