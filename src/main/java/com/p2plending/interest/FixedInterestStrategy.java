package com.p2plending.interest;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Implementasi Strategy untuk bunga tetap (Fixed Rate).
 */
public class FixedInterestStrategy implements InterestStrategy {
    private final BigDecimal rate;

    public FixedInterestStrategy(BigDecimal rate) {
        this.rate = rate;
    }

    @Override
    public BigDecimal calculate(BigDecimal principal, int tenorInMonths) {
        // Asumsi kalkulasi sederhana: Pokok x Persentase Tetap
        return principal.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }
}