package com.p2plending.domain.loan.decorator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PlatformFeeDecorator extends LoanDecorator {
    private final BigDecimal fee;

    public PlatformFeeDecorator(LoanCost inner, BigDecimal fee) {
        super(inner);
        this.fee = fee.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getCost() {
        return super.getCost().add(this.fee);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " + Biaya Platform";
    }
}
