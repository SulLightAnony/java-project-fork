package com.p2plending.domain.loan;

import java.math.BigDecimal;

public class Borrower {
    private final String id;
    private BigDecimal remainingLimit;

    public Borrower(String id, BigDecimal initialLimit) {
        this.id = id;
        this.remainingLimit = initialLimit;
    }

    public String getId() {
        return id;
    }

    // Algoritma Pengecekan Limit
    public boolean canBorrow(BigDecimal amount) {
        // compareTo menghasilkan -1 jika remainingLimit < amount
        return remainingLimit.compareTo(amount) >= 0;
    }

    // Algoritma Pengurangan Limit
    public void decreaseLimit(BigDecimal amount) {
        if (!canBorrow(amount)) {
            throw new IllegalArgumentException("Limit pinjaman tidak mencukupi");
        }
        this.remainingLimit = this.remainingLimit.subtract(amount);
    }
}