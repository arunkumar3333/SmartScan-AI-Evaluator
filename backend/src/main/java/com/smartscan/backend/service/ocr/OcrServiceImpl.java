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

        return answerSheet.getExtractedText() == null ? "" : answerSheet.getExtractedText();
    }

    @Override
    public String extractText(File file) throws Exception {

        configureTesseract(); // ✅ IMPORTANT

        return tesseract.doOCR(file);
    }
}