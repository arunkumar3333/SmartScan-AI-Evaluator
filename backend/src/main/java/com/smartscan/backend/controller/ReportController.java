package com.smartscan.backend.controller;

import com.smartscan.backend.service.reporting.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {
        return reportService.dashboard();
    }

    @GetMapping("/charts")
    public List<Map<String, Object>> charts() {
        return reportService.charts();
    }

    @GetMapping("/export")
    public String export() {
        return reportService.exportCSV();
    }
}