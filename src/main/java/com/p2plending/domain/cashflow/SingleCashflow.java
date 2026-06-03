package com.p2plending.domain.cashflow;

import java.math.BigDecimal;

/**
 * Leaf dalam Composite Pattern.
 * Merepresentasikan satu komponen cashflow (misal: hanya pokok, atau hanya bunga).
 */
public class SingleCashflow implements Cashflow {

    private final CashflowType type;
    private final BigDecimal amount;

    public SingleCashflow(CashflowType type, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cashflow tidak boleh negatif");
        }
        this.type   = type;
        this.amount = amount;
    }

    @Override
    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public CashflowType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "SingleCashflow{type=" + type + ", amount=" + amount + "}";
    }
}