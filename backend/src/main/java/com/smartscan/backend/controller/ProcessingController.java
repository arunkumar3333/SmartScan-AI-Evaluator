package com.smartscan.backend.controller;
import com.smartscan.backend.service.processing.ProcessingService;
import com.smartscan.backend.dto.ProcessingResponseDto;
import com.smartscan.backend.dto.ProcessingStatusResponseDto;
import com.smartscan.backend.entity.AnswerSheet;
import com.smartscan.backend.repository.AnswerSheetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/process")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProcessingController {

    private final ProcessingService processingService;
    private final AnswerSheetRepository answerSheetRepository;

    @PostMapping("/upload")
    public List<ProcessingResponseDto> uploadMultiple(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam(value = "teacherId", required = false) Long teacherId,
            @RequestParam(value = "studentName", required = false, defaultValue = "Unknown Student") String studentName
    ) throws Exception {

        List<ProcessingResponseDto> results = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) {
                continue;
            }

            AnswerSheet sheet = processingService.processAndSave(file, teacherId, studentName);

            results.add(
                    ProcessingResponseDto.builder()
                            .id(sheet.getId())
                            .fileName(sheet.getFileName())
                            .status(sheet.getStatus())
                            .score(sheet.getScore())
                            .build()
            );
        }

        return results;
    }

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