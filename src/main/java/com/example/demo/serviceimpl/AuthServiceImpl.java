
package com.example.demo.serviceimpl;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.UserProfileResponse;
import com.example.demo.entity.Admin;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Worker;
import com.example.demo.repository.AdminRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.WorkerRepository;
import com.example.demo.security.JwtService;
import com.example.demo.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final CustomerRepository customerRepository;
    private final AdminRepository adminRepository;
    private final WorkerRepository workerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(
            CustomerRepository customerRepository,
            AdminRepository adminRepository,
            WorkerRepository workerRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.customerRepository = customerRepository;
        this.adminRepository = adminRepository;
        this.workerRepository = workerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    private ResponseStatusException invalidCredentials() {
        return new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Invalid email, password or role");
    }

    private boolean passwordMatches(
            String rawPassword,
            String encodedPassword) {

        return encodedPassword != null
                && passwordEncoder.matches(
                        rawPassword, encodedPassword);
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        if (request == null
                || request.getEmail() == null
                || request.getEmail().isBlank()
                || request.getPassword() == null
                || request.getPassword().isBlank()
                || request.getRole() == null) {

            throw invalidCredentials();
        }

        String email = request.getEmail().trim();
        String password = request.getPassword();
        String role = request.getRole().trim();

        int userId;
        String name;

        switch (role) {

            case "CUSTOMER":

                Customer customer = customerRepository
                        .findByEmail(email)
                        .orElseThrow(this::invalidCredentials);

                if (!passwordMatches(
                        password, customer.getPassword())) {
                    throw invalidCredentials();
                }

                userId = customer.getCustomerId();
                name = customer.getCustomerName();
                break;

            case "ADMIN":

                Admin admin = adminRepository
                        .findByEmail(email)
                        .orElseThrow(this::invalidCredentials);

                if (!passwordMatches(
                        password, admin.getPassword())) {
                    throw invalidCredentials();
                }

                userId = admin.getAdminId();
                name = admin.getAdminName();
                break;

            case "WORKER":

                Worker worker = workerRepository
                        .findByEmail(email)
                        .orElseThrow(this::invalidCredentials);

                if (!passwordMatches(
                        password, worker.getPassword())) {
                    throw invalidCredentials();
                }

                // Workers must be approved before login.
                if (!"APPROVED".equals(
                        worker.getVerificationStatus())) {

                    throw new ResponseStatusException(
                            HttpStatus.FORBIDDEN,
                            "Worker account is not approved");
                }

                userId = worker.getWorkerId();
                name = worker.getWorkerName();
                break;

            default:
                throw invalidCredentials();
        }

        String token = jwtService.generateToken(userId, role);

        return new LoginResponse(
                "Login successful",
                token,
                role,
                userId,
                name);
    }

    @Override
    public UserProfileResponse getProfile(
            int userId,
            String role) {

        switch (role) {

            case "CUSTOMER":

                Customer customer = customerRepository
                        .findById(userId)
                        .orElseThrow(this::invalidCredentials);

                return new UserProfileResponse(
                        customer.getCustomerId(),
                        customer.getCustomerName(),
                        customer.getEmail(),
                        "CUSTOMER");

            case "ADMIN":

                Admin admin = adminRepository
                        .findById(userId)
                        .orElseThrow(this::invalidCredentials);

                return new UserProfileResponse(
                        admin.getAdminId(),
                        admin.getAdminName(),
                        admin.getEmail(),
                        "ADMIN");

            case "WORKER":

                Worker worker = workerRepository
                        .findById(userId)
                        .orElseThrow(this::invalidCredentials);

                if (!"APPROVED".equals(
                        worker.getVerificationStatus())) {
                    throw new ResponseStatusException(
                            HttpStatus.FORBIDDEN,
                            "Worker account is not approved");
                }

                return new UserProfileResponse(
                        worker.getWorkerId(),
                        worker.getWorkerName(),
                        worker.getEmail(),
                        "WORKER");

            default:
                throw invalidCredentials();
        }
    }
}
