package com.smartscan.backend.service.admin;

import com.smartscan.backend.dto.AdminDashboardDto;
import com.smartscan.backend.dto.TeacherApprovalDto;

import java.util.List;

public interface AdminService {

    // Get dashboard statistics
    AdminDashboardDto getDashboardStats();

    // Get all teachers
    List<TeacherApprovalDto> getAllTeachers();

    // Get pending teachers only
    List<TeacherApprovalDto> getPendingTeachers();

    // Approve teacher
    String approveTeacher(Long teacherId);

    // Disapprove teacher
    String disapproveTeacher(Long teacherId);

    // Delete teacher completely
    String removeTeacher(Long teacherId);
}