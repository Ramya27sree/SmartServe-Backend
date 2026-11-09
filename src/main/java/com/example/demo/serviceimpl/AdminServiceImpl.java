
package com.example.demo.serviceimpl;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entity.Admin;
import com.example.demo.repository.AdminRepository;
import com.example.demo.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminServiceImpl(AdminRepository adminRepository,
                            PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Admin registerAdmin(Admin admin) {

        // 1. Check request
        if (admin == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Admin details are required"
            );
        }

        // 2. Validate admin name
        if (admin.getAdminName() == null ||
            admin.getAdminName().isBlank()) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Admin name is required"
            );
        }

        // 3. Validate email
        if (admin.getEmail() == null ||
            admin.getEmail().isBlank()) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Email is required"
            );
        }

        // 4. Validate password
        if (admin.getPassword() == null ||
            admin.getPassword().isBlank()) {

            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Password is required"
            );
        }

        // 5. Check duplicate email
        if (adminRepository.existsByEmail(admin.getEmail())) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Email already registered"
            );
        }

        // 6. Set admin details
        admin.setAdminId(0);
        admin.setRole("ADMIN");

        // 7. Encrypt password
        admin.setPassword(
            passwordEncoder.encode(admin.getPassword())
        );

        // 8. Save admin in MySQL
        return adminRepository.save(admin);
    }
}
