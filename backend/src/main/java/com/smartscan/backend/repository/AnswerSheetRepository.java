package com.smartscan.backend.repository;

import com.smartscan.backend.entity.AnswerSheet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerSheetRepository extends JpaRepository<AnswerSheet, Long> {

    List<AnswerSheet> findByTeacherId(Long teacherId);

    long countByTeacherId(Long teacherId);
}