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
import java.util.Map;
import com.smartscan.backend.service.ai.OllamaService;
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
    private final ImageProcessingService imageService; // ✅ FIXED
    private final QuestionRepository questionRepository;
    private final OllamaService ollamaService; 

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

        return repository.save(sheet);
    }

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

            //String modelAnswer = "Artificial Intelligence is the simulation of human intelligence in machines.";
           var question = questionRepository
    .findById(sheet.getQuestionId())
    .orElseThrow(() -> new RuntimeException("Question not found"));

String modelAnswer = question.getModelAnswer();

sheet.setModelName(question.getTitle());
            // AI grading
            GradingResult result = graderService.evaluate(studentAnswer, modelAnswer);

            // Save
            sheet.setExtractedText(text);
            //sheet.setScore(result.getScore());
            Map<String, Object> aiResult = ollamaService.generateFeedback(
            studentAnswer,
            modelAnswer
            );

            sheet.setScore((Integer) aiResult.get("finalScore"));
            sheet.setSimilarity(Double.valueOf((Integer) aiResult.get("similarityScore")));
            sheet.setLlmScore((Integer) aiResult.get("llmScore"));            sheet.setFeedback((String) aiResult.get("feedback"));
            //sheet.setSimilarity(result.getSimilarity());
            //sheet.setFeedback(result.getFeedback());
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