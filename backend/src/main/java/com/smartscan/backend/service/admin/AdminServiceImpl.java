package com.smartscan.backend.service.admin;

import com.smartscan.backend.dto.AdminDashboardDto;
import com.smartscan.backend.dto.TeacherApprovalDto;
import com.smartscan.backend.entity.Role;
import com.smartscan.backend.entity.User;
import com.smartscan.backend.repository.AnswerSheetRepository;
import com.smartscan.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    // Repository for uploads
    private final AnswerSheetRepository answerSheetRepository;

    // Repository for users
    private final UserRepository userRepository;

    @Override
    public AdminDashboardDto getDashboardStats() {
        return AdminDashboardDto.builder()
                .totalUploads(answerSheetRepository.count())
                .totalTeachers(userRepository.countByRole(Role.TEACHER))
                .build();
    }

    @Override
    public List<TeacherApprovalDto> getAllTeachers() {
        return userRepository.findByRole(Role.TEACHER)
                .stream()
                .map(this::mapToTeacherDto)
                .toList();
    }

    @Override
    public List<TeacherApprovalDto> getPendingTeachers() {
        return userRepository.findByRoleAndApproved(Role.TEACHER, false)
                .stream()
                .map(this::mapToTeacherDto)
                .toList();
    }

    @Override
    public String approveTeacher(Long teacherId) {
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        if (teacher.getRole() != Role.TEACHER) {
            throw new RuntimeException("User is not a teacher");
        }

        teacher.setApproved(true);
        userRepository.save(teacher);

        return "Teacher approved successfully";
    }

    @Override
    public String disapproveTeacher(Long teacherId) {
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        if (teacher.getRole() != Role.TEACHER) {
            throw new RuntimeException("User is not a teacher");
        }

        teacher.setApproved(false);
        userRepository.save(teacher);

        return "Teacher disapproved successfully";
    }

    @Override
    public String removeTeacher(Long teacherId) {
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        if (teacher.getRole() != Role.TEACHER) {
            throw new RuntimeException("User is not a teacher");
        }

        userRepository.delete(teacher);

        return "Teacher removed successfully";
    }

    // Convert User entity into TeacherApprovalDto
    private TeacherApprovalDto mapToTeacherDto(User user) {
        return TeacherApprovalDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .approved(user.isApproved())
                .build();
    }
}