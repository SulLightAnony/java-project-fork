package com.p2plending.interest;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Implementasi Strategy untuk bunga mengambang (Floating Rate).
 * Bunga dihitung dari Base Rate (Suku Bunga Acuan) + Margin Rate.
 */
public class FloatingInterestStrategy implements InterestStrategy {
    private final BigDecimal baseRate;
    private final BigDecimal marginRate;

    public FloatingInterestStrategy(BigDecimal baseRate, BigDecimal marginRate) {
        this.baseRate = baseRate;
        this.marginRate = marginRate;
    }

    @Override
    public BigDecimal calculate(BigDecimal principal, int tenorInMonths) {
        // Total persentase = Suku Bunga Acuan + Margin Keuntungan Platform
        BigDecimal totalRate = baseRate.add(marginRate);
        
        // Asumsi kalkulasi sederhana: Pokok x Total Persentase
        return principal.multiply(totalRate).setScale(2, RoundingMode.HALF_UP);
    }
}