package com.smartscan.backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDashboardDto {

    // Total uploads count
    private long totalUploads;

    // Total teachers count
    private long totalTeachers;
}