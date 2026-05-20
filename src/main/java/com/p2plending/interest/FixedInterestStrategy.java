package com.p2plending.interest;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FixedInterestStrategy implements InterestStrategy {
    private final BigDecimal rate;

    public FixedInterestStrategy(BigDecimal rate) {
        if (rate == null || rate.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Rate must be non-negative");
        }
        this.rate = rate;
    }

    @Override
    public BigDecimal calculate(BigDecimal principal, int tenorInMonths) {
        if (principal == null || principal.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Principal must be non-negative");
        }
        return principal.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }
}
