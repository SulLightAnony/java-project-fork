package com.p2plending.domain.disbursement;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

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

        this.id = UUID.randomUUID().toString();
        this.loanId = loanId;
        this.disbursedAmount = disbursedAmount;
        this.disbursementDate = LocalDateTime.now();
        this.repaymentSchedule = repaymentSchedule;
    }

    public String getId() {
        return id;
    }

    public String getLoanId() {
        return loanId;
    }

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
