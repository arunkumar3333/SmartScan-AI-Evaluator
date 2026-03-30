package com.smartscan.backend.controller;

import com.smartscan.backend.service.ocr.OcrService;
import com.smartscan.backend.service.ProcessingService;
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
    private final ProcessingService processingService;

    @PostMapping("/process/{answerSheetId}")
    public ResponseEntity<?> processAnswerSheet(@PathVariable Long answerSheetId) {

        String result = processingService.process(answerSheetId);

        return ResponseEntity.ok(
                Map.of(
                        "message", "Processing completed",
                        "answerSheetId", String.valueOf(answerSheetId),
                        "result", result
                )
        );
    }

    @GetMapping("/{answerSheetId:\\d+}")
    public ResponseEntity<Map<String, String>> getExtractedText(@PathVariable Long answerSheetId) {

        String extractedText = ocrService.getExtractedText(answerSheetId);

        return ResponseEntity.ok(
                Map.of(
                        "answerSheetId", String.valueOf(answerSheetId),
                        "extractedText", extractedText
                )
        );
    }

    @GetMapping("/test")
    public String test() {
        return "OCR working";
    }
}