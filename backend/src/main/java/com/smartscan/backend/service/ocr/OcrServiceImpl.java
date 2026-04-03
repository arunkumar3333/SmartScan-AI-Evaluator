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

    // ✅ Apply config ONCE (constructor or init)
    private void configureTesseract() {
        tesseract.setPageSegMode(1); // automatic segmentation
        tesseract.setOcrEngineMode(3); // best engine
        tesseract.setVariable("user_defined_dpi", "300");

        // tesseract.setVariable("tessedit_char_whitelist",
        //         "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789.,:-() ");
    }

    @Override
    public String getExtractedText(Long answerSheetId) {


        AnswerSheet answerSheet = answerSheetRepository.findById(answerSheetId)
                .orElseThrow(() -> new RuntimeException("Answer sheet not found with id: " + answerSheetId));

        AnswerSheet sheet = answerSheetRepository.findById(answerSheetId)
                .orElseThrow(() -> new RuntimeException("Answer sheet not found: " + answerSheetId));

        return sheet.getExtractedText() == null ? "" : sheet.getExtractedText();
    }

    @Override

    public String extractText(File file) throws Exception {

        configureTesseract(); // ✅ IMPORTANT

        return tesseract.doOCR(file);
    public String extractText(File file) {

        if (file == null || !file.exists()) {
            throw new RuntimeException("Invalid file for OCR");
        }

        try {
            ITesseract tesseract = new Tesseract();

            //  IMPORTANT: Update if your path is different
            tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");

            // Language
            tesseract.setLanguage("eng");

            //  UPDATED (non-deprecated method)
            tesseract.setVariable("user_defined_dpi", "300");
            tesseract.setVariable("preserve_interword_spaces", "1");

            // Better recognition modes
            tesseract.setOcrEngineMode(1);  // LSTM
            tesseract.setPageSegMode(3);   // Fully automatic page segmentation

            String result = tesseract.doOCR(file);

            // Clean result
            result = result.replaceAll("[^\\x00-\\x7F]", ""); // remove weird chars
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