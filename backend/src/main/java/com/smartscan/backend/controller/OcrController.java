package com.smartscan.backend.controller;

import com.smartscan.backend.dto.ExtractedTextResponseDto;
import com.smartscan.backend.service.ocr.OcrService;
import com.smartscan.backend.service.processing.ProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ocr")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OcrController {

    private final OcrService ocrService;

    //  NEW (IMPORTANT)
    private final ProcessingService processingService;

    //  Get OCR extracted text
    @GetMapping("/{answerSheetId}")
    public ExtractedTextResponseDto getExtractedText(@PathVariable Long answerSheetId) {
        return ExtractedTextResponseDto.builder()
                .answerSheetId(answerSheetId)
                .extractedText(ocrService.getExtractedText(answerSheetId))
                .build();
    }

    //  Test API
    @GetMapping("/test")
    public String test() {
        return "OCR working";
    }

    //  PHASE 3 API (VERY IMPORTANT)
    @GetMapping("/process/{answerSheetId}")
    public String process(@PathVariable Long answerSheetId) {
        return processingService.process(answerSheetId);
    }
}