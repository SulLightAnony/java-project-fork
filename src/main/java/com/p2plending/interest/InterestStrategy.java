package com.p2plending.interest;

import java.math.BigDecimal;

public interface InterestStrategy {
    BigDecimal calculate(BigDecimal principal, int tenorInMonths);
}
