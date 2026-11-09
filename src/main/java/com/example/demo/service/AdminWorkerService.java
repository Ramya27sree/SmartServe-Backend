
package com.example.demo.service;

import java.util.List;
import com.example.demo.dto.WorkerApprovalResponse;

public interface AdminWorkerService {

    List<WorkerApprovalResponse> getPendingWorkers();

    WorkerApprovalResponse approveWorker(int workerId);

    WorkerApprovalResponse rejectWorker(int workerId);
}
