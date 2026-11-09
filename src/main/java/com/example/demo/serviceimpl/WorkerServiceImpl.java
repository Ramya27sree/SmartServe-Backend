
package com.example.demo.serviceimpl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Worker;
import com.example.demo.repository.WorkerRepository;
import com.example.demo.service.WorkerService;

@Service
public class WorkerServiceImpl implements WorkerService {

    private final WorkerRepository workerRepository;
    private final PasswordEncoder passwordEncoder;

    public WorkerServiceImpl(
            WorkerRepository workerRepository,
            PasswordEncoder passwordEncoder) {

        this.workerRepository = workerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Worker registerWorker(Worker worker) {

        if (workerRepository.existsByEmail(worker.getEmail())) {
            throw new IllegalArgumentException(
                "Email already registered"
            );
        }

        // Database generates the worker ID
        worker.setWorkerId(0);

        // New workers must be verified by an admin
        worker.setVerificationStatus("PENDING");

        // Workers cannot accept bookings immediately
        worker.setAvailabilityStatus("UNAVAILABLE");

        // Assign the role on the server
        worker.setRole("WORKER");

        // Encrypt password using BCrypt
        worker.setPassword(
            passwordEncoder.encode(worker.getPassword())
        );

        // Save worker to MySQL
        return workerRepository.save(worker);
    }
}
