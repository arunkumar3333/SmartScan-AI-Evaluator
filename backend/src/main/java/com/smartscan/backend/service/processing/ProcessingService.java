package com.smartscan.backend.service.processing;

import com.smartscan.backend.dto.GradingResult;
import com.smartscan.backend.entity.AnswerSheet;
import com.smartscan.backend.repository.AnswerSheetRepository;
import com.smartscan.backend.service.ocr.OcrService;
import com.smartscan.backend.util.PDFUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
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
    private final GraderService graderService;

    // ============================
    // STEP 1: UPLOAD ONLY
    // ============================
    public AnswerSheet saveOnly(MultipartFile file, Long teacherId, String studentName) throws Exception {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is missing or empty");
        }

        String uploadPath = System.getProperty("user.dir") + "/uploads/";
        File dir = new File(uploadPath);
        if (!dir.exists()) dir.mkdirs();

        String originalName = file.getOriginalFilename();
        if (originalName == null || originalName.isBlank()) {
            originalName = "uploaded_file";
        }

        String fileName = System.currentTimeMillis() + "_" + originalName.replaceAll("\\s+", "_");

        File savedFile = new File(dir, fileName);
        file.transferTo(savedFile);

        AnswerSheet sheet = AnswerSheet.builder()
                .teacherId(teacherId)
                .studentName(studentName)
                .fileName(fileName)
                .fileType(file.getContentType())
                .filePath(savedFile.getAbsolutePath())
                .status("UPLOADED")
                .uploadTime(LocalDateTime.now())
                .build();

                processAsync(sheet.getId());
        return repository.save(sheet);
        
    }

    // ============================
    // STEP 2: BACKGROUND PROCESSING
    // ============================
    @Async
    public void processAsync(Long sheetId) {

        try {
            AnswerSheet sheet = repository.findById(sheetId)
                    .orElseThrow(() -> new RuntimeException("Answer sheet not found"));

            sheet.setStatus("PROCESSING");
            repository.save(sheet);

            File savedFile = new File(sheet.getFilePath());

            // PDF → Image
            File fileToProcess;
            if (sheet.getFileName().toLowerCase().endsWith(".pdf")) {
                fileToProcess = PDFUtil.convertPdfToImage(savedFile);
                if (fileToProcess == null || !fileToProcess.exists()) {
                    throw new RuntimeException("PDF conversion failed");
                }
            } else {
                fileToProcess = savedFile;
            }

            // OCR
            String text = ocrService.extractText(fileToProcess);
            if (text == null) text = "";

            // SEGMENT answers
            List<String> answers = segment(text);

            // AI Evaluation
            String modelAnswer = "Artificial Intelligence is the simulation of human intelligence in machines.";

            int totalScore = 0;

            for (String ans : answers) {
                GradingResult result = graderService.evaluate(ans, modelAnswer);
                totalScore += result.getScore();
            }

            // SAVE
            sheet.setExtractedText(text);
            sheet.setScore(totalScore);
            sheet.setStatus("PROCESSED");

            repository.save(sheet);

        } catch (Exception e) {
            e.printStackTrace();

            AnswerSheet sheet = repository.findById(sheetId).orElse(null);
            if (sheet != null) {
                sheet.setStatus("FAILED");
                repository.save(sheet);
            }
        }
    }

    // ============================
    // STEP 3: PROCESS API (USED IN POSTMAN)
    // ============================
    public String process(Long answerSheetId) {

        try {
            AnswerSheet sheet = repository.findById(answerSheetId)
                    .orElseThrow(() -> new RuntimeException("Answer sheet not found"));

            String text = sheet.getExtractedText();

            if (text == null || text.isEmpty()) {
                text = ocrService.extractText(new File(sheet.getFilePath()));
            }

            String modelAnswer = "Artificial Intelligence is the simulation of human intelligence in machines.";

            GradingResult result = graderService.evaluate(text, modelAnswer);

            sheet.setScore(result.getScore());
            sheet.setStatus("PROCESSED");

            repository.save(sheet);

            return "Score: " + result.getScore()
                    + "\nFeedback: " + result.getFeedback()
                    + "\nSimilarity: " + Math.round(result.getSimilarity() * 100) + "%";

        } catch (Exception e) {
            throw new RuntimeException("Processing failed: " + e.getMessage());
        }
    }

    // ============================
    // SEGMENTATION LOGIC
    // ============================
    public List<String> segment(String text) {

        List<String> answers = new ArrayList<>();

        if (text == null || text.trim().isEmpty()) {
            return answers;
        }

        String[] parts = text.split("(?i)Question\\s*\\d+");

        for (String part : parts) {
            if (part.toLowerCase().contains("answer")) {
                String[] ans = part.split("(?i)Answer:");
                if (ans.length > 1) {
                    answers.add(ans[1].trim());
                }
            }
        }

        if (answers.isEmpty()) {
            answers.add(text.trim());
        }

        return answers;
    }
}