package com.smartscan.backend.controller;

import com.smartscan.backend.entity.AnswerSheet;
import com.smartscan.backend.service.ProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/process")
public class ProcessingController {

    @Autowired
    private ProcessingService processingService;

    @PostMapping("/upload")
    public List<Object> uploadMultiple(@RequestParam("file") MultipartFile[] files) throws Exception {

        List<Object> results = new ArrayList<>();

        for (MultipartFile file : files) {

            if (file == null || file.isEmpty()) {
                continue;
            }

            // Call service (ALL LOGIC IS HERE)
            AnswerSheet sheet = processingService.processAndSave(file);

            results.add(Map.of(
                    "id", sheet.getId(),
                    "fileName", sheet.getFileName(),
                    "score", sheet.getScore(),
                    "status", sheet.getStatus()
            ));
        }

        return results;
    }
}