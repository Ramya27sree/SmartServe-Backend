
package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.Worker;

public interface WorkerRepository
        extends JpaRepository<Worker, Integer> {

    boolean existsByEmail(String email);

    Optional<Worker> findByEmail(String email);

    List<Worker> findByVerificationStatus(String status);
}
