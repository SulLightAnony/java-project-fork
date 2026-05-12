package com.p2plending.domain.funding;

import java.math.BigDecimal;

public class LenderContribution {

    private final String lenderId;
    private final BigDecimal amount;

    public LenderContribution(String lenderId, BigDecimal amount) {
        this.lenderId = lenderId;
        this.amount = amount;
    }

    public String getLenderId() {
        return lenderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}