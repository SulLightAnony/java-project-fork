package com.p2plending.interest;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
        // Menggunakan persentase margin bagi hasil yang sudah disepakati di awal
        return principal.multiply(marginBagiHasil).setScale(2, RoundingMode.HALF_UP);
    }
}