package com.smartscan.backend.service.admin;

import com.smartscan.backend.dto.AdminDashboardDto;

public interface AdminService {
    AdminDashboardDto getDashboardStats();
}