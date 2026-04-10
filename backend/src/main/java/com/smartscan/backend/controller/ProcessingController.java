package com.smartscan.backend.controller;

import com.smartscan.backend.service.processing.ProcessingService;
import com.smartscan.backend.dto.ProcessingResponseDto;
import com.smartscan.backend.dto.ProcessingStatusResponseDto;
import com.smartscan.backend.entity.AnswerSheet;
import com.smartscan.backend.repository.AnswerSheetRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/process")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProcessingController {

    private final ProcessingService processingService;
    private final AnswerSheetRepository answerSheetRepository;

    // SINGLE FILE UPLOAD (FINAL)
    @PostMapping("/upload")
    public ProcessingResponseDto upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "teacherId", required = false) Long teacherId,
            @RequestParam(value = "studentName", defaultValue = "Unknown Student") String studentName,
            @RequestParam("questionId") Long questionId
    ) throws Exception {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        // Save file only
        AnswerSheet sheet = processingService.saveOnly(file, teacherId, studentName, questionId);

        // Trigger async processing
        processingService.processAsync(sheet.getId());

        // Return response
        return ProcessingResponseDto.builder()
                .id(sheet.getId())
                .fileName(sheet.getFileName())
                .status("UPLOADED")
                .build();
    }

    // CHECK PROCESSING STATUS
    @GetMapping("/status/{answerSheetId}")
    public ProcessingStatusResponseDto getProcessingStatus(@PathVariable Long answerSheetId) {

        AnswerSheet sheet = answerSheetRepository.findById(answerSheetId)
                .orElseThrow(() -> new RuntimeException("Answer sheet not found"));

        return ProcessingStatusResponseDto.builder()
                .answerSheetId(sheet.getId())
                .fileName(sheet.getFileName())
                .status(sheet.getStatus())
                .build();
    }
}