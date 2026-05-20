package com.p2plending.interest;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
        return principal.multiply(marginBagiHasil).setScale(2, RoundingMode.HALF_UP);
    }
}