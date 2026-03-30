package com.smartscan.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "answer_sheets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long teacherId;
    private String studentName;

    private String fileName;
    private String fileType;
    private String filePath;

    private String status; // UPLOADED, PROCESSED

    @Column(length = 5000) // 🔥 important for long OCR text
    private String extractedText;

    private Integer score;

    private LocalDateTime uploadTime;
}