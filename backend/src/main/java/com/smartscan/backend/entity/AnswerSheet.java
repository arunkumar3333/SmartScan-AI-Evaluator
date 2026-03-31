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

    // UPLOADED, PROCESSING, PROCESSED, FAILED
    private String status;

    @Column(length = 10000)
    private String extractedText;

    private Integer score;

    private LocalDateTime uploadTime;
}