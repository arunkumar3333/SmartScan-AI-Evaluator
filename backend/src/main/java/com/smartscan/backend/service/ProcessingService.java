package com.smartscan.backend.service;

import com.smartscan.backend.entity.AnswerSheet;
import com.smartscan.backend.repository.AnswerSheetRepository;
import com.smartscan.backend.service.ocr.OcrService;
import com.smartscan.backend.util.PDFUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProcessingService {

    private final AnswerSheetRepository repository;
    private final OcrService ocrService;
    private final ImageProcessingService imageService;
    private final EvaluationService evaluationService;

    // MAIN METHOD
    public AnswerSheet processAndSave(MultipartFile file) throws Exception {

        // 1. VALIDATION
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is missing or empty");
        }

        // 2. CREATE UPLOAD DIRECTORY
        String uploadPath = System.getProperty("user.dir") + "/uploads/";
        File dir = new File(uploadPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 3. SAFE FILE NAME
        String originalName = file.getOriginalFilename();
        if (originalName == null || originalName.isEmpty()) {
            originalName = "uploaded_file";
        }

        String fileName = System.currentTimeMillis() + "_" +
                originalName.replaceAll("\\s+", "_");

        File savedFile = new File(dir, fileName);
        file.transferTo(savedFile);

        System.out.println("Saved file path: " + savedFile.getAbsolutePath());

        // 4. SAVE INITIAL DB ENTRY
        AnswerSheet sheet = AnswerSheet.builder()
                .fileName(fileName)
                .fileType(file.getContentType())
                .filePath(savedFile.getAbsolutePath())
                .status("UPLOADED")
                .uploadTime(LocalDateTime.now())
                .build();

        sheet = repository.save(sheet);

        // 5. DETERMINE FILE TYPE
        File fileToProcess;

        if (fileName.toLowerCase().endsWith(".pdf")) {
            // ✅ Convert PDF → Image
            fileToProcess = PDFUtil.convertPdfToImage(savedFile);

            if (fileToProcess == null || !fileToProcess.exists()) {
                throw new RuntimeException("PDF conversion failed");
            }

            System.out.println("PDF converted to image: " + fileToProcess.getAbsolutePath());
        } else {
            // ✅ Direct image
            fileToProcess = savedFile;
        }

        // 6. OPEN-CV PROCESSING (ONLY IMAGE NOW)
        File processed = imageService.preprocess(fileToProcess);

        if (processed == null || !processed.exists()) {
            throw new RuntimeException("Image preprocessing failed");
        }

        // 7. OCR
        String text = ocrService.extractText(processed);

        if (text == null || text.trim().isEmpty()) {
            text = "";
        }

        System.out.println("OCR Extracted Text:\n" + text);

        // 8. SEGMENTATION
        List<String> answers = segment(text);

        // 9. EVALUATION
        int score = evaluationService.evaluate(answers);

        // 10. UPDATE DB
        sheet.setExtractedText(text);
        sheet.setScore(score);
        sheet.setStatus("PROCESSED");

        return repository.save(sheet);
    }

    // SEGMENTATION LOGIC (IMPROVED)
    public List<String> segment(String text) {

        List<String> answers = new ArrayList<>();

        if (text == null || text.trim().isEmpty()) {
            return answers;
        }

        // Try structured format first
        String[] parts = text.split("(?i)Question\\s*\\d+");

        for (String p : parts) {
            if (p.toLowerCase().contains("answer")) {

                String[] ans = p.split("(?i)Answer:");

                if (ans.length > 1) {
                    answers.add(ans[1].trim());
                }
            }
        }

        // Fallback if nothing detected
        if (answers.isEmpty()) {
            answers.add(text.trim());
        }

        return answers;
    }

    // EXISTING METHOD (KEEP FOR DEV1 COMPATIBILITY)
    public String process(Long answerSheetId) {
        return "Processed answer sheet with ID: " + answerSheetId;
    }
}