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

    // ✅ AI fields (ONLY ONCE)
    private Integer score;
    private Integer llmScore;
    private Double similarity;

    @Column(length = 5000)
    private String feedback;

    // File info
    private String fileName;
    private String fileType;
    private String filePath;

    private String status;

    @Column(length = 10000)
    private String extractedText;

    private LocalDateTime uploadTime;
}