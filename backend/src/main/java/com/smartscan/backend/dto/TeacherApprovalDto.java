package com.smartscan.backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherApprovalDto {

    // Teacher id
    private Long id;

    // Teacher name
    private String name;

    // Teacher email
    private String email;

    // Role
    private String role;

    // Approval status
    private boolean approved;
}