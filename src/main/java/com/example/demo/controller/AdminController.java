
package com.example.demo.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.Admin;
import com.example.demo.service.AdminService;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerAdmin(
            @RequestBody Admin admin) {

        Admin savedAdmin =
                adminService.registerAdmin(admin);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                    "adminId", savedAdmin.getAdminId(),
                    "adminName", savedAdmin.getAdminName(),
                    "message", "Admin registered successfully"
                ));
    }
}
