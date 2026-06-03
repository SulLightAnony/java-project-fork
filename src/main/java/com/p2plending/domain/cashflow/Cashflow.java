package com.p2plending.domain.cashflow;

import java.math.BigDecimal;

/**
 * Component interface untuk Composite Pattern.
 * Berlaku sama untuk SingleCashflow (Leaf) maupun LoanCashflow (Composite).
 */
public interface Cashflow {
    BigDecimal getAmount();
    CashflowType getType();
}