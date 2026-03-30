package com.smartscan.backend.service.ocr;

import java.io.File;

public interface OcrService {

    String extractTextFromAnswerSheet(Long answerSheetId);
    String getExtractedText(Long answerSheetId);

    // Phase 2
    String extractText(File file) throws Exception;
}