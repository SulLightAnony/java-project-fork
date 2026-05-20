package com.p2plending.domain.disbursement;

import java.math.BigDecimal;

public class RepaymentSchedule {
    private final BigDecimal totalAmountToRepay;
    private final BigDecimal monthlyInstallment;

    public RepaymentSchedule(BigDecimal totalAmountToRepay, BigDecimal monthlyInstallment) {
        this.totalAmountToRepay = totalAmountToRepay;
        this.monthlyInstallment = monthlyInstallment;
    }

    public BigDecimal getTotalAmountToRepay() {
        return totalAmountToRepay;
    }

    public BigDecimal getMonthlyInstallment() {
        return monthlyInstallment;
    }
}
