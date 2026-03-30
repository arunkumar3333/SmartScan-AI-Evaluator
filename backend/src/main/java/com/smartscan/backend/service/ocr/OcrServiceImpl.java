package com.smartscan.backend.service.ocr;

import com.smartscan.backend.entity.AnswerSheet;
import com.smartscan.backend.repository.AnswerSheetRepository;
import lombok.RequiredArgsConstructor;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
public class OcrServiceImpl implements OcrService {

    private final AnswerSheetRepository answerSheetRepository;
    private final Tesseract tesseract;

    // ✅ Dev1 method
    @Override
    public String extractTextFromAnswerSheet(Long answerSheetId) {
        AnswerSheet answerSheet = answerSheetRepository.findById(answerSheetId)
                .orElseThrow(() -> new RuntimeException("Answer sheet not found with id: " + answerSheetId));

        return "OCR extracted text from file: " + answerSheet.getFilePath();
    }

    // ✅ Dev1 method
    @Override
    public String getExtractedText(Long answerSheetId) {
        AnswerSheet answerSheet = answerSheetRepository.findById(answerSheetId)
                .orElseThrow(() -> new RuntimeException("Answer sheet not found with id: " + answerSheetId));

        return "OCR text for file: " + answerSheet.getFileName();
    }

    @Override
    public String extractText(File file) throws Exception {
        return tesseract.doOCR(file);
    }
}