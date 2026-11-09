
package com.example.demo.dto;

public record WorkerApprovalResponse(
        int workerId,
        String workerName,
        String email,
        String city,
        String serviceType,
        int experience,
        String verificationStatus
) {
}
