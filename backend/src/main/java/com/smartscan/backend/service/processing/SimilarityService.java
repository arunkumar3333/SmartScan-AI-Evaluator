package com.smartscan.backend.service.processing;

import org.springframework.stereotype.Service;

import java.util.*;
@Service
public class SimilarityService {

    private static final Set<String> STOP_WORDS = Set.of(
            "is", "the", "of", "and", "a", "to", "in", "that", "it", "on", "for"
    );

    public double cosineSimilarity(String text1, String text2) {

        Map<String, Integer> freq1 = getWordFreq(text1);
        Map<String, Integer> freq2 = getWordFreq(text2);

        Set<String> allWords = new HashSet<>();
        allWords.addAll(freq1.keySet());
        allWords.addAll(freq2.keySet());

        double dot = 0, mag1 = 0, mag2 = 0;

        for (String word : allWords) {
            int v1 = freq1.getOrDefault(word, 0);
            int v2 = freq2.getOrDefault(word, 0);

            dot += v1 * v2;
            mag1 += v1 * v1;
            mag2 += v2 * v2;
        }

        if (mag1 == 0 || mag2 == 0) return 0;

        return dot / (Math.sqrt(mag1) * Math.sqrt(mag2));
    }

    private Map<String, Integer> getWordFreq(String text) {
        Map<String, Integer> map = new HashMap<>();

        String[] words = text.toLowerCase().split("\\W+");

        for (String word : words) {
            if (!word.isBlank() && !STOP_WORDS.contains(word)) {
                map.put(word, map.getOrDefault(word, 0) + 1);
            }
        }

        return map;
    }
}