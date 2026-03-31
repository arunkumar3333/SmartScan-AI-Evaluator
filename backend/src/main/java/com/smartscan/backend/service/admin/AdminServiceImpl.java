package com.smartscan.backend.service.admin;

import com.smartscan.backend.dto.AdminDashboardDto;
import com.smartscan.backend.entity.Role;
import com.smartscan.backend.repository.AnswerSheetRepository;
import com.smartscan.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AnswerSheetRepository answerSheetRepository;
    private final UserRepository userRepository;

    @Override
    public AdminDashboardDto getDashboardStats() {
        return AdminDashboardDto.builder()
                .totalUploads(answerSheetRepository.count())
                .totalTeachers(userRepository.countByRole(Role.TEACHER))
                .build();
    }
}