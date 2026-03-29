package com.smartscan.backend.service.ocr;

import com.smartscan.backend.entity.AnswerSheet;
import com.smartscan.backend.repository.AnswerSheetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OcrServiceImpl implements OcrService {

    private final AnswerSheetRepository answerSheetRepository;

    @Override
    public String extractTextFromAnswerSheet(Long answerSheetId) {
        AnswerSheet answerSheet = answerSheetRepository.findById(answerSheetId)
                .orElseThrow(() -> new RuntimeException("Answer sheet not found with id: " + answerSheetId));

        String extractedText = "OCR extracted text from file: " + answerSheet.getFilePath();

        return extractedText;
    }

    @Override
    public String getExtractedText(Long answerSheetId) {
        AnswerSheet answerSheet = answerSheetRepository.findById(answerSheetId)
                .orElseThrow(() -> new RuntimeException("Answer sheet not found with id: " + answerSheetId));

        return "OCR text for file: " + answerSheet.getFileName();
    }
}