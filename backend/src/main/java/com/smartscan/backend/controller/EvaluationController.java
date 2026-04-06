package com.smartscan.backend.controller;

import com.smartscan.backend.dto.EvaluationResultResponseDto;
import com.smartscan.backend.entity.AnswerSheet;
import com.smartscan.backend.repository.AnswerSheetRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/evaluation")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EvaluationController {

    private final AnswerSheetRepository answerSheetRepository;

    @GetMapping("/{answerSheetId}")
    public EvaluationResultResponseDto getEvaluationResult(@PathVariable Long answerSheetId) {

        AnswerSheet sheet = answerSheetRepository.findById(answerSheetId)
                .orElseThrow(() -> new RuntimeException("Answer sheet not found"));

        return EvaluationResultResponseDto.builder()
                .answerSheetId(sheet.getId())
                .fileName(sheet.getFileName())
                .score(sheet.getScore())
                .similarity(sheet.getSimilarity())
                .llmScore(sheet.getLlmScore()) 
                .feedback(sheet.getFeedback())
                .status(sheet.getStatus())
                .build();
    }
}