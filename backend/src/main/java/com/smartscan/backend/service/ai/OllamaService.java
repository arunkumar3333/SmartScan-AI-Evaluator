package com.smartscan.backend.service.ai;

import com.smartscan.backend.service.processing.EmbeddingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OllamaService {

    // Inject properly here
    private final EmbeddingService embeddingService;
    //Call external API, sends http request
    public Map<String, Object> generateFeedback(String studentAnswer, String modelAnswer) {

        String url = "http://localhost:11434/api/generate";
        RestTemplate restTemplate = new RestTemplate();

        String prompt = """
                You are an AI exam evaluator.

                Compare the student answer with the model answer.

                Give output strictly in this format:
                Score: <number out of 10>
                Feedback: <clear explanation>

                Student Answer:
                """ + studentAnswer + """

                Model Answer:
                """ + modelAnswer;

        // Creating Request data
        Map<String, Object> request = new HashMap<>();
        request.put("model", "phi3");//Which AI model to use
        request.put("prompt", prompt);// Sending full instruction to AI
        request.put("stream", false);//Give full response at once

        //Sends POST request to Ollama
        Map response = restTemplate.postForObject(url, request, Map.class);

        String result = response.get("response").toString();

        // Extract LLM score
        int llmScore = 5;
        try {
            // extracts LLM score from AI response text
            llmScore = Integer.parseInt(
                    result.split("Score:")[1]
                          .split("/")[0]
                          .trim()
            );
        } catch (Exception ignored) {}

        // Embedding similarity
        double similarity = embeddingService.calculateSimilarity(studentAnswer, modelAnswer);
        int similarityScore = (int) Math.round(similarity * 10);

        // Final score
        int finalScore = (llmScore + similarityScore) / 2;

        //Return structured response
        Map<String, Object> finalResponse = new HashMap<>();
        finalResponse.put("llmScore", llmScore);
        finalResponse.put("similarityScore", similarityScore);
        finalResponse.put("finalScore", finalScore);
        finalResponse.put("feedback", result);

        return finalResponse;
    }
}