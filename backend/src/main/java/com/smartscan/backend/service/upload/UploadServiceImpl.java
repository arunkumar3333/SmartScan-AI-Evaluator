package com.smartscan.backend.service.upload;

import com.smartscan.backend.dto.UploadResponseDto;
import com.smartscan.backend.entity.AnswerSheet;
import com.smartscan.backend.repository.AnswerSheetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UploadServiceImpl implements UploadService {

    private final AnswerSheetRepository answerSheetRepository;
    private static final String UPLOAD_DIR = "uploads";

    @Override
    public UploadResponseDto uploadFile(MultipartFile file, Long teacherId, String studentName) {
        try {
            if (file == null || file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            Files.createDirectories(Paths.get(UPLOAD_DIR));

            String originalFileName = file.getOriginalFilename();
            String safeFileName = UUID.randomUUID() + "_" + originalFileName;
            Path filePath = Paths.get(UPLOAD_DIR, safeFileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            AnswerSheet answerSheet = AnswerSheet.builder()
                    .teacherId(teacherId)
                    .studentName(studentName)
                    .fileName(originalFileName)
                    .fileType(file.getContentType())
                    .filePath(filePath.toString())
                    .status("UPLOADED")
                    .uploadTime(LocalDateTime.now())
                    .build();

            AnswerSheet saved = answerSheetRepository.save(answerSheet);
            return mapToDto(saved);

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + e.getMessage());
        }
    }

    @Override
    public List<UploadResponseDto> getAllUploads() {
        return answerSheetRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UploadResponseDto getUploadById(Long id) {
        AnswerSheet answerSheet = answerSheetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Upload not found with id: " + id));
        return mapToDto(answerSheet);
    }

    @Override
    public void deleteUpload(Long id) {
        AnswerSheet answerSheet = answerSheetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Upload not found with id: " + id));

        try {
            Files.deleteIfExists(Paths.get(answerSheet.getFilePath()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file: " + e.getMessage());
        }

        answerSheetRepository.delete(answerSheet);
    }

    private UploadResponseDto mapToDto(AnswerSheet answerSheet) {
        return UploadResponseDto.builder()
                .id(answerSheet.getId())
                .studentName(answerSheet.getStudentName())
                .fileName(answerSheet.getFileName())
                .fileType(answerSheet.getFileType())
                .filePath(answerSheet.getFilePath())
                .status(answerSheet.getStatus())
                .uploadTime(answerSheet.getUploadTime())
                .build();
    }
}