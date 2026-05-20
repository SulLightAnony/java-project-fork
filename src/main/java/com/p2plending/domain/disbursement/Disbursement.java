package com.p2plending.domain.disbursement;

import java.math.BigDecimal;

public class Disbursement {
    private final String loanId;
    private final BigDecimal amount;
    private final BigDecimal interest;
    private final RepaymentSchedule repaymentSchedule;

    public Disbursement(String loanId, BigDecimal amount, BigDecimal interest, RepaymentSchedule repaymentSchedule) {
        this.loanId = loanId;
        this.amount = amount;
        this.interest = interest;
        this.repaymentSchedule = repaymentSchedule;
    }

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entitas yang mencatat rekam jejak pencairan dana ke Borrower.
 */
public class Disbursement {
    private final String id;
    private final String loanId;
    private final BigDecimal disbursedAmount;
    private final LocalDateTime disbursementDate;
    private final RepaymentSchedule repaymentSchedule;

    public Disbursement(String loanId, BigDecimal disbursedAmount, RepaymentSchedule repaymentSchedule) {
        if (disbursedAmount == null || disbursedAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Jumlah pencairan harus lebih besar dari 0.");
        }

        this.id = UUID.randomUUID().toString(); // Generate ID unik secara otomatis
        this.loanId = loanId;
        this.disbursedAmount = disbursedAmount;
        this.disbursementDate = LocalDateTime.now(); // Tanggal pencairan adalah saat objek ini dibuat
        this.repaymentSchedule = repaymentSchedule;
    }

    public String getId() {
        return id;
    }

    public String getLoanId() {
        return loanId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getInterest() {
        return interest;
    public BigDecimal getDisbursedAmount() {
        return disbursedAmount;
    }

    public LocalDateTime getDisbursementDate() {
        return disbursementDate;
    }

    public RepaymentSchedule getRepaymentSchedule() {
        return repaymentSchedule;
    }
}
}
