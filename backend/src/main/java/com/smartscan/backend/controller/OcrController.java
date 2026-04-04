package com.smartscan.backend.controller;

import com.smartscan.backend.dto.ExtractedTextResponseDto;
import com.smartscan.backend.service.ocr.OcrService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ocr")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OcrController {

    private final OcrService ocrService;

    @GetMapping("/{answerSheetId}")
    public ExtractedTextResponseDto getExtractedText(@PathVariable Long answerSheetId) {
        return ExtractedTextResponseDto.builder()
                .answerSheetId(answerSheetId)
                .extractedText(ocrService.getExtractedText(answerSheetId))
                .build();
    }

    @GetMapping("/test")
    public String test() {
        return "OCR working";
    }
}