package com.smartscan.backend.service.reporting;

import com.smartscan.backend.repository.AnswerSheetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final AnswerSheetRepository repo;

    public Map<String, Object> dashboard() {

        long total = repo.count();

        Map<String, Object> data = new HashMap<>();

        data.put("totalSubmissions", total);
        data.put("averageScore", 7);
        data.put("passRate", "70%");

        return data;
    }

    public List<Map<String, Object>> charts() {

        List<Map<String, Object>> list = new ArrayList<>();

        list.add(Map.of("topic", "AI", "score", 8));
        list.add(Map.of("topic", "ML", "score", 6));

        return list;
    }

    public String exportCSV() {
        return "Name,Score\nArun,8\nRavi,6";
    }
}