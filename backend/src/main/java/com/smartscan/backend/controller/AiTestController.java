package com.smartscan.backend.controller;
import java.util.Map;
import com.smartscan.backend.service.ai.OllamaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class AiTestController {

    private final OllamaService ollamaService;
    @PostMapping("/grade")
public Map<String, Object> testGrading(@RequestBody TestRequest request) {
    return ollamaService.generateFeedback(
            request.getStudentAnswer(),
            request.getModelAnswer()
    );
}
}

class TestRequest {
    private String studentAnswer;
    private String modelAnswer;

    public String getStudentAnswer() { return studentAnswer; }
    public void setStudentAnswer(String studentAnswer) { this.studentAnswer = studentAnswer; }

    public String getModelAnswer() { return modelAnswer; }
    public void setModelAnswer(String modelAnswer) { this.modelAnswer = modelAnswer; }
}