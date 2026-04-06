package com.smartscan.backend.controller;

import com.smartscan.backend.dto.UploadResponseDto;
import com.smartscan.backend.service.upload.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UploadController {

    private final UploadService uploadService;

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UploadResponseDto> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("teacherId") Long teacherId,
            @RequestParam("studentName") String studentName,
            @RequestParam("questionId") Long questionId
    ) {
        UploadResponseDto response = uploadService.uploadFile(file, teacherId, studentName, questionId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UploadResponseDto>> getAllUploads() {
        return ResponseEntity.ok(uploadService.getAllUploads());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UploadResponseDto> getUploadById(@PathVariable Long id) {
        return ResponseEntity.ok(uploadService.getUploadById(id));
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<String> deleteUpload(@PathVariable Long id) {
        uploadService.deleteUpload(id);
        return ResponseEntity.ok("File deleted successfully");
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<UploadResponseDto>> getUploadsByTeacherId(@PathVariable Long teacherId) {
        return ResponseEntity.ok(uploadService.getUploadsByTeacherId(teacherId));
    }

    @GetMapping("/teacher/{teacherId}/count")
    public ResponseEntity<Long> countUploadsByTeacherId(@PathVariable Long teacherId) {
        return ResponseEntity.ok(uploadService.countUploadsByTeacherId(teacherId));
    }
}