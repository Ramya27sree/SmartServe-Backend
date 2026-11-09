
package com.example.demo.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.Worker;
import com.example.demo.service.WorkerService;

@RestController
@RequestMapping("/api/workers")
public class WorkerController {

    private final WorkerService workerService;

    public WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerWorker(
            @RequestBody Worker worker) {

        Worker savedWorker =
                workerService.registerWorker(worker);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                    "workerId", savedWorker.getWorkerId(),
                    "workerName", savedWorker.getWorkerName(),
                    "verificationStatus",
                        savedWorker.getVerificationStatus(),
                    "message", "Worker registered successfully"
                ));
    }
}
