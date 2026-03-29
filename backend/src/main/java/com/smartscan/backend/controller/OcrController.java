package com.smartscan.backend.controller;

import com.smartscan.backend.service.ocr.OcrService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ocr")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OcrController {

    private final OcrService ocrService;

    @PostMapping("/extract/{answerSheetId}")
    public ResponseEntity<Map<String, String>> extractText(@PathVariable Long answerSheetId) {
        String extractedText = ocrService.extractTextFromAnswerSheet(answerSheetId);

        return ResponseEntity.ok(
                Map.of(
                        "message", "Text extracted successfully",
                        "answerSheetId", String.valueOf(answerSheetId),
                        "extractedText", extractedText
                )
        );
    }

    @GetMapping("/{answerSheetId}")
    public ResponseEntity<Map<String, String>> getExtractedText(@PathVariable Long answerSheetId) {
        String extractedText = ocrService.getExtractedText(answerSheetId);

        return ResponseEntity.ok(
                Map.of(
                        "answerSheetId", String.valueOf(answerSheetId),
                        "extractedText", extractedText
                )
        );
    }
}