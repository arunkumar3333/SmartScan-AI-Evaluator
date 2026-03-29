package com.smartscan.backend.service.ocr;

public interface OcrService {
    String extractTextFromAnswerSheet(Long answerSheetId);
    String getExtractedText(Long answerSheetId);
}