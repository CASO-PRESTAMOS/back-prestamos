package com.example.caso_prestamos.Service.Impl;

import com.example.caso_prestamos.Domain.Entity.Admin;
import com.example.caso_prestamos.Repository.AdminRepository;
import com.example.caso_prestamos.Service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void updatePassword(String password) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Admin admin = adminRepository.findByUsername(username).
                orElseThrow(() -> new RuntimeException("Admin not found"));
        admin.setPassword(passwordEncoder.encode(password));
        adminRepository.save(admin);
    }
}
