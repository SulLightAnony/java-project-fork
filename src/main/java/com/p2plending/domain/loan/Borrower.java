package com.p2plending.domain.loan;

import java.math.BigDecimal;

public class Borrower {
    private final String id;
    private final String name;
    private final int creditScore;
    private BigDecimal remainingLimit;
    private boolean isActive;

    public Borrower(String id, String name, int creditScore, BigDecimal initialLimit) {
        this.id = id;
        this.name = name;
        this.creditScore = creditScore;
        this.remainingLimit = initialLimit;
        this.isActive = true;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public BigDecimal getRemainingLimit() {
        return remainingLimit;
    }

    public boolean isActive() {
        return isActive;
    }

    // logika bisnis

    // cek apakah limit mencukupi
    public boolean isEligible() {
        return isActive && creditScore >= 500;
    }

    // cek apakah limit mencukupi untuk jumlah yang diajukan
    public boolean canBorrow(BigDecimal amount) {
        return remainingLimit.compareTo(amount) >= 0;
    }

    // kurangi limit setelah pinjaman disetujui
    public void decreaseLimit(BigDecimal amount) {
        if (!canBorrow(amount)) {
            throw new IllegalArgumentException("Limit pinjaman tidak mencukupi");
        }
        this.remainingLimit = this.remainingLimit.subtract(amount);
    }

    // kembalikan limit setelah peminjaman
    public void restoreLimit(BigDecimal amount) {
        this.remainingLimit = this.remainingLimit.add(amount);
    }

    public void deactive() {
        this.isActive = false;
    }
}