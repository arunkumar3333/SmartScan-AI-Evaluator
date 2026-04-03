package com.smartscan.backend.service.ocr;

import com.smartscan.backend.entity.AnswerSheet;
import com.smartscan.backend.repository.AnswerSheetRepository;
import lombok.RequiredArgsConstructor;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
public class OcrServiceImpl implements OcrService {

    private final AnswerSheetRepository answerSheetRepository;

    @Override
    public String getExtractedText(Long answerSheetId) {

        AnswerSheet sheet = answerSheetRepository.findById(answerSheetId)
                .orElseThrow(() -> new RuntimeException("Answer sheet not found: " + answerSheetId));

        return sheet.getExtractedText() == null ? "" : sheet.getExtractedText();
    }

    @Override
    public String extractText(File file) {

        if (file == null || !file.exists()) {
            throw new RuntimeException("Invalid file for OCR");
        }

        try {
            ITesseract tesseract = new Tesseract();

            // IMPORTANT: change if your path differs
            tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");

            tesseract.setLanguage("eng");

            tesseract.setVariable("user_defined_dpi", "300");
            tesseract.setVariable("preserve_interword_spaces", "1");

            tesseract.setOcrEngineMode(1);
            tesseract.setPageSegMode(3);

            String result = tesseract.doOCR(file);

            // Clean output
            result = result.replaceAll("[^\\x00-\\x7F]", "");
            result = result.replaceAll("\\s+", " ").trim();

            System.out.println("\n========= OCR OUTPUT =========");
            System.out.println(result);
            System.out.println("==============================\n");

            return result;

        } catch (TesseractException e) {
            throw new RuntimeException("OCR failed: " + e.getMessage());
        }
    }
}