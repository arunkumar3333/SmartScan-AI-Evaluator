package com.smartscan.backend.controller;

import com.smartscan.backend.dto.QuestionRequestDto;
import com.smartscan.backend.entity.Question;
import com.smartscan.backend.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List; // ✅ ADD THIS

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
@CrossOrigin("*")
public class QuestionController {

    private final QuestionRepository questionRepository;

    // ✅ Create question + model answer
    @PostMapping
    public Question createQuestion(@RequestBody QuestionRequestDto dto) {

        Question question = Question.builder()
                .title(dto.getTitle()) // ✅ model name (Science, Math, etc.)
                .questionText(dto.getQuestionText())
                .modelAnswer(dto.getModelAnswer())
                .teacherId(dto.getTeacherId())
                .build();

        return questionRepository.save(question);
    }

    // ✅ Get single question
    @GetMapping("/{id}")
    public Question getQuestion(@PathVariable Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));
    }

    // ✅ Get all questions (FOR DROPDOWN)
    @GetMapping
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }
}