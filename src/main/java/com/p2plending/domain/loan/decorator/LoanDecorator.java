package com.p2plending.domain.loan.decorator;

import java.math.BigDecimal;

public abstract class LoanDecorator implements LoanCost {
    protected final LoanCost inner;

    protected LoanDecorator(LoanCost inner) {
        this.inner = inner;
    }

    @Override
    public BigDecimal getCost() {
        return inner.getCost();
    }

    @Override
    public String getDescription() {
        return inner.getDescription();
    }
}
