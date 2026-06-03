package com.p2plending.domain.loan.decorator;

import java.math.BigDecimal;

public interface LoanCost {
    BigDecimal getCost();
    String getDescription();
}
