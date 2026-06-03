package com.p2plending.domain.disbursement;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RepaymentSchedule {
    private final BigDecimal totalAmountToRepay; 
    private final int tenorInMonths;
    private final BigDecimal monthlyInstallment;

    public RepaymentSchedule(BigDecimal principal, BigDecimal totalInterest, int tenorInMonths) {
        if (tenorInMonths <= 0) {
            throw new IllegalArgumentException("Tenor harus lebih dari 0 bulan.");
        }
        
        this.totalAmountToRepay = principal.add(totalInterest);
        this.tenorInMonths = tenorInMonths;
        
        this.monthlyInstallment = this.totalAmountToRepay.divide(new BigDecimal(tenorInMonths), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotalAmountToRepay() {
        return totalAmountToRepay;
    }

    public BigDecimal getMonthlyInstallment() {
        return monthlyInstallment;
    }

    public int getTenorInMonths() {
        return tenorInMonths;
    }
}
