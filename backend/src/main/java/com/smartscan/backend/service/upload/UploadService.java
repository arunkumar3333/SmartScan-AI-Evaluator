package com.smartscan.backend.service.upload;

import com.smartscan.backend.dto.UploadResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UploadService {
    UploadResponseDto uploadFile(MultipartFile file, Long teacherId, String studentName);
    List<UploadResponseDto> getAllUploads();
    UploadResponseDto getUploadById(Long id);
    void deleteUpload(Long id);
    List<UploadResponseDto> getUploadsByTeacherId(Long teacherId);
long countUploadsByTeacherId(Long teacherId);
}