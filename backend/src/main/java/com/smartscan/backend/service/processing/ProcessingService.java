package com.smartscan.backend.service.processing;

import com.smartscan.backend.dto.GradingResult;
import com.smartscan.backend.entity.AnswerSheet;
import com.smartscan.backend.repository.AnswerSheetRepository;
import com.smartscan.backend.repository.QuestionRepository;
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

    private final GraderService graderService;
    private final AnswerSheetRepository repository;
    private final OcrService ocrService;
<<<<<<< HEAD
    private final GraderService graderService;

    // ============================
    // STEP 1: UPLOAD ONLY
    // ============================
    public AnswerSheet saveOnly(MultipartFile file, Long teacherId, String studentName) throws Exception {
=======
    private final ImageProcessingService imageService; // ✅ FIXED
    private final QuestionRepository questionRepository;
>>>>>>> dev1

public AnswerSheet saveOnly(MultipartFile file, Long teacherId, String studentName, Long questionId) throws Exception {
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
                .questionId(questionId)
                .uploadTime(LocalDateTime.now())
                .build();

                processAsync(sheet.getId());
        return repository.save(sheet);
        
    }

<<<<<<< HEAD
    // ============================
    // STEP 2: BACKGROUND PROCESSING
    // ============================
=======
>>>>>>> dev1
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
            } else {
                fileToProcess = savedFile;
            }

<<<<<<< HEAD
            // OCR
            String text = ocrService.extractText(fileToProcess);
            if (text == null) text = "";

            // SEGMENT answers
            List<String> answers = segment(text);

            // AI Evaluation
            String modelAnswer = "Artificial Intelligence is the simulation of human intelligence in machines.";
=======
            // ✅ PREPROCESS + OCR
            File processedFile = imageService.preprocess(fileToProcess);
            String text = ocrService.extractText(processedFile);

            if (text == null) text = "";

            // ✅ CLEAN TEXT
            text = text.replaceAll("[^a-zA-Z0-9\\s]", " ");
            text = text.replaceAll("\\s+", " ").trim();

            // Segmentation
            List<String> answers = segment(text);

            String studentAnswer = String.join(" ", answers);
>>>>>>> dev1

            //String modelAnswer = "Artificial Intelligence is the simulation of human intelligence in machines.";
            String modelAnswer = questionRepository
        .findById(sheet.getQuestionId())
        .orElseThrow(() -> new RuntimeException("Question not found"))
        .getModelAnswer();
            // AI grading
            GradingResult result = graderService.evaluate(studentAnswer, modelAnswer);

<<<<<<< HEAD
            for (String ans : answers) {
                GradingResult result = graderService.evaluate(ans, modelAnswer);
                totalScore += result.getScore();
            }

            // SAVE
=======
            // Save
>>>>>>> dev1
            sheet.setExtractedText(text);
            sheet.setScore(result.getScore());
            sheet.setSimilarity(result.getSimilarity());
            sheet.setFeedback(result.getFeedback());
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

<<<<<<< HEAD
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
=======
>>>>>>> dev1
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