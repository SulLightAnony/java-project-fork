package com.p2plending.domain.loan.decorator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SimpleLoan implements LoanCost {
    private final BigDecimal principal;

    public SimpleLoan(BigDecimal principal) {
        this.principal = principal.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getCost() {
        return this.principal;
    }

    @Override
    public String getDescription() {
        return "Pokok Pinjaman";
    }
}
