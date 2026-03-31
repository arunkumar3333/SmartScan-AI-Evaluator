package com.smartscan.backend.controller;

import com.smartscan.backend.dto.AdminDashboardDto;
import com.smartscan.backend.dto.TeacherApprovalDto;
import com.smartscan.backend.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminController {

    // Inject admin service
    private final AdminService adminService;

    // Dashboard stats
    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardDto> getDashboardStats() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

    // Get all teachers
    @GetMapping("/teachers")
    public ResponseEntity<List<TeacherApprovalDto>> getAllTeachers() {
        return ResponseEntity.ok(adminService.getAllTeachers());
    }

    // Get pending teachers
    @GetMapping("/teachers/pending")
    public ResponseEntity<List<TeacherApprovalDto>> getPendingTeachers() {
        return ResponseEntity.ok(adminService.getPendingTeachers());
    }

    // Approve teacher
    @PutMapping("/teachers/{teacherId}/approve")
    public ResponseEntity<String> approveTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(adminService.approveTeacher(teacherId));
    }

    // Disapprove teacher
    @PutMapping("/teachers/{teacherId}/disapprove")
    public ResponseEntity<String> disapproveTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(adminService.disapproveTeacher(teacherId));
    }

    // Delete teacher
    @DeleteMapping("/teachers/{teacherId}")
    public ResponseEntity<String> removeTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(adminService.removeTeacher(teacherId));
    }
}