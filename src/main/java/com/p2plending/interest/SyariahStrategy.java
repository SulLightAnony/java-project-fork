package com.p2plending.interest;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SyariahStrategy implements InterestStrategy {
    private final BigDecimal margin;

    public SyariahStrategy(BigDecimal margin) {
        if (margin == null || margin.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Margin must be non-negative");
        }
        this.margin = margin;
/**
 * Implementasi Strategy untuk pembiayaan Syariah (contoh: margin Murabahah).
 */
public class SyariahStrategy implements InterestStrategy {
    private final BigDecimal marginBagiHasil;

    public SyariahStrategy(BigDecimal marginBagiHasil) {
        this.marginBagiHasil = marginBagiHasil;
    }

    @Override
    public BigDecimal calculate(BigDecimal principal, int tenorInMonths) {
        if (principal == null || principal.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Principal must be non-negative");
        }
        return principal.multiply(margin).setScale(2, RoundingMode.HALF_UP);
        // Menggunakan persentase margin bagi hasil yang sudah disepakati di awal
        return principal.multiply(marginBagiHasil).setScale(2, RoundingMode.HALF_UP);
    }
}