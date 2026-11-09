
package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.WorkerApprovalResponse;
import com.example.demo.service.AdminWorkerService;

@RestController
@RequestMapping("/api/admin/workers")
public class AdminWorkerController {

    private final AdminWorkerService adminWorkerService;

    public AdminWorkerController(
            AdminWorkerService adminWorkerService) {
        this.adminWorkerService = adminWorkerService;
    }

    @GetMapping("/pending")
    public List<WorkerApprovalResponse> getPendingWorkers() {
        return adminWorkerService.getPendingWorkers();
    }

    @PutMapping("/{id}/approve")
    public WorkerApprovalResponse approveWorker(
            @PathVariable("id") int workerId) {

        return adminWorkerService.approveWorker(workerId);
    }

    @PutMapping("/{id}/reject")
    public WorkerApprovalResponse rejectWorker(
            @PathVariable("id") int workerId) {

        return adminWorkerService.rejectWorker(workerId);
    }
}
