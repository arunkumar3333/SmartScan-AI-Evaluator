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
    private Long questionId;
    private Long teacherId;
    private String studentName;

    private String fileName;
    private String fileType;
    private String filePath;

    private String status;

    @Column(length = 10000)
    private String extractedText;

    private Integer score;

    // ✅ ADD THESE
    private Double similarity;

    @Column(length = 5000)
    private String feedback;

    private LocalDateTime uploadTime;
}