package com.smartscan.backend.service.ocr;

import java.io.File;

public interface OcrService {

    String getExtractedText(Long answerSheetId);

    String extractText(File file) throws Exception;
}