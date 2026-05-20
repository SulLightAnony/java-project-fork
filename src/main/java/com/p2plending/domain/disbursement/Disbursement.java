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

    public String getLoanId() {
        return loanId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public RepaymentSchedule getRepaymentSchedule() {
        return repaymentSchedule;
    }
}
