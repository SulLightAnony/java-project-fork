package com.p2plending.risk;

public class ValidationResult {
    private final boolean approved;
    private final String rejectionReason;

    private ValidationResult(boolean approved, String rejectionReason) {
        this.approved = approved;
        this.rejectionReason = rejectionReason;
    }

    public static ValidationResult approve() {
        return new ValidationResult(true, null);
    }

    public static ValidationResult reject(String reason) {
        return new ValidationResult(false, reason);
    }

    public boolean isApproved() { return approved; }
    public String getRejectionReason() { return rejectionReason; }
}