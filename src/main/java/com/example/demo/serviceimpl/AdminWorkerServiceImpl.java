
package com.example.demo.serviceimpl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.WorkerApprovalResponse;
import com.example.demo.entity.Worker;
import com.example.demo.repository.WorkerRepository;
import com.example.demo.service.AdminWorkerService;

@Service
public class AdminWorkerServiceImpl
        implements AdminWorkerService {

    private final WorkerRepository workerRepository;

    public AdminWorkerServiceImpl(
            WorkerRepository workerRepository) {
        this.workerRepository = workerRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkerApprovalResponse> getPendingWorkers() {

        return workerRepository
                .findByVerificationStatus("PENDING")
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public WorkerApprovalResponse approveWorker(int workerId) {

        Worker worker = findPendingWorker(workerId);

        worker.setVerificationStatus("APPROVED");

        Worker savedWorker = workerRepository.save(worker);

        return toResponse(savedWorker);
    }

    @Override
    @Transactional
    public WorkerApprovalResponse rejectWorker(int workerId) {

        Worker worker = findPendingWorker(workerId);

        worker.setVerificationStatus("REJECTED");

        Worker savedWorker = workerRepository.save(worker);

        return toResponse(savedWorker);
    }

    private Worker findPendingWorker(int workerId) {

        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Worker not found"
                ));

        if (!"PENDING".equals(worker.getVerificationStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Only pending workers can be reviewed"
            );
        }

        return worker;
    }

    private WorkerApprovalResponse toResponse(Worker worker) {

        return new WorkerApprovalResponse(
                worker.getWorkerId(),
                worker.getWorkerName(),
                worker.getEmail(),
                worker.getCity(),
                worker.getServiceType(),
                worker.getExperience(),
                worker.getVerificationStatus()
        );
    }
}
