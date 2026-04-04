package com.smartscan.backend.controller;

import com.smartscan.backend.service.processing.ProcessingService;
import com.smartscan.backend.dto.ProcessingResponseDto;
import com.smartscan.backend.dto.ProcessingStatusResponseDto;
import com.smartscan.backend.entity.AnswerSheet;
import com.smartscan.backend.repository.AnswerSheetRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/process")
@CrossOrigin(origins = "*")
public class ProcessingController {

    private final ProcessingService processingService;
    private final AnswerSheetRepository answerSheetRepository;

    // MANUAL CONSTRUCTOR
    public ProcessingController(ProcessingService processingService,
                                AnswerSheetRepository answerSheetRepository) {
        this.processingService = processingService;
        this.answerSheetRepository = answerSheetRepository;
    }

    // MULTI-FILE UPLOAD 
    @PostMapping("/upload")
    public List<ProcessingResponseDto> upload(
            @RequestParam("file") MultipartFile[] files, // Changed to Array to support the loop
            @RequestParam(value = "teacherId", required = false) Long teacherId,
            @RequestParam(value = "studentName", defaultValue = "Unknown Student") String studentName,
            @RequestParam("questionId") Long questionId
    ) throws Exception {

        List<ProcessingResponseDto> results = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) continue;

            AnswerSheet sheet = processingService.saveOnly(file, teacherId, studentName);
            processingService.processAsync(sheet.getId());

            results.add(
                    ProcessingResponseDto.builder()
                            .id(sheet.getId())
                            .fileName(sheet.getFileName())
                            .status(sheet.getStatus())
                            .score(sheet.getScore())
                            .build()
            );
        }
        return results; // Added the missing return statement
    } // Added the missing closing bracket for the method

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

    // PROCESS API
    @GetMapping("/{id}")
    public String process(@PathVariable Long id) {
        return processingService.process(id);
    }
}