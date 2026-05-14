package com.p2plending.domain.disbursement;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Entitas yang merepresentasikan jadwal pembayaran pinjaman.
 */
public class RepaymentSchedule {
    private final BigDecimal totalAmountToRepay; // Pokok + Bunga
    private final int tenorInMonths;
    private final BigDecimal monthlyInstallment;

    public RepaymentSchedule(BigDecimal principal, BigDecimal totalInterest, int tenorInMonths) {
        if (tenorInMonths <= 0) {
            throw new IllegalArgumentException("Tenor harus lebih dari 0 bulan.");
        }
        
        this.totalAmountToRepay = principal.add(totalInterest);
        this.tenorInMonths = tenorInMonths;
        
        // Asumsi cicilan bulanan rata: (Pokok + Bunga) / Tenor
        this.monthlyInstallment = this.totalAmountToRepay.divide(new BigDecimal(tenorInMonths), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotalAmountToRepay() {
        return totalAmountToRepay;
    }

    public int getTenorInMonths() {
        return tenorInMonths;
    }

    public BigDecimal getMonthlyInstallment() {
        return monthlyInstallment;
    }
}