package com.smartscan.backend.repository;

import com.smartscan.backend.entity.AnswerSheet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerSheetRepository extends JpaRepository<AnswerSheet, Long> {
}