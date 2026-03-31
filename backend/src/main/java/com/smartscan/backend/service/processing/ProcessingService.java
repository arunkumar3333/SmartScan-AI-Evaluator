package com.smartscan.backend.service.processing;

import com.smartscan.backend.entity.AnswerSheet;
import com.smartscan.backend.repository.AnswerSheetRepository;
import com.smartscan.backend.service.ocr.OcrService;
import com.smartscan.backend.util.PDFUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.smartscan.backend.service.evaluation.EvaluationService;
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

    public AnswerSheet processAndSave(MultipartFile file, Long teacherId, String studentName) throws Exception {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is missing or empty");
        }

        String uploadPath = System.getProperty("user.dir") + "/uploads/";
        File dir = new File(uploadPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

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

        sheet = repository.save(sheet);

        try {
            sheet.setStatus("PROCESSING");
            sheet = repository.save(sheet);

            File fileToProcess;

            if (fileName.toLowerCase().endsWith(".pdf")) {
                fileToProcess = PDFUtil.convertPdfToImage(savedFile);

                if (fileToProcess == null || !fileToProcess.exists()) {
                    throw new RuntimeException("PDF conversion failed");
                }
            } else {
                fileToProcess = savedFile;
            }

            File processed = imageService.preprocess(fileToProcess);

            if (processed == null || !processed.exists()) {
                throw new RuntimeException("Image preprocessing failed");
            }

            String text = ocrService.extractText(processed);
            if (text == null || text.trim().isEmpty()) {
                text = "";
            }

            List<String> answers = segment(text);
            int score = evaluationService.evaluate(answers);

            sheet.setExtractedText(text);
            sheet.setScore(score);
            sheet.setStatus("PROCESSED");

            return repository.save(sheet);

        } catch (Exception e) {
            sheet.setStatus("FAILED");
            repository.save(sheet);
            throw e;
        }
    }

    public List<String> segment(String text) {
        List<String> answers = new ArrayList<>();

        if (text == null || text.trim().isEmpty()) {
            return answers;
        }

        String[] parts = text.split("(?i)Question\\s*\\d+");

        for (String p : parts) {
            if (p.toLowerCase().contains("answer")) {
                String[] ans = p.split("(?i)Answer:");
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
