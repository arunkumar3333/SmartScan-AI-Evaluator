package com.smartscan.backend.service.processing;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class EmbeddingService {

    private final RestTemplate restTemplate = new RestTemplate();

    public double calculateSimilarity(String text1, String text2) {
        // converting text into numbers so computer can understand meaning
        List<Double> emb1 = getEmbedding(text1);
        List<Double> emb2 = getEmbedding(text2);

        return cosineSimilarity(emb1, emb2);
    }

    private List<Double> getEmbedding(String text) {

        String url = "http://localhost:11434/api/embeddings";

        Map<String, Object> request = Map.of(
                "model", "phi3",
                "prompt", text
        );

        Map response = restTemplate.postForObject(url, request, Map.class);

        return (List<Double>) response.get("embedding");
    }

    private double cosineSimilarity(List<Double> v1, List<Double> v2) {

        double dot = 0, mag1 = 0, mag2 = 0;

        for (int i = 0; i < v1.size(); i++) {
            dot += v1.get(i) * v2.get(i);
            mag1 += v1.get(i) * v1.get(i);
            mag2 += v2.get(i) * v2.get(i);
        }

        if (mag1 == 0 || mag2 == 0) return 0;

        return dot / (Math.sqrt(mag1) * Math.sqrt(mag2));
    }
}