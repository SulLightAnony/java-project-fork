package com.p2plending.domain.loan;

import com.p2plending.domain.loan.state.DraftState;
import com.p2plending.domain.loan.state.LoanState;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Loan {
    private final String id;
    private final Borrower borrower;
    private final BigDecimal amount;
    private final int tenor;
    private final String purpose;
    private final LocalDateTime createdAt;

    private LoanStatus status;
    private LoanState stateBehavior;

    public Loan(String id, Borrower borrower, BigDecimal amount, int tenor, String purpose) {
        // Validasi Domain: Mencegah pembuatan objek jika limit tidak cukup
        if (!borrower.canBorrow(amount)) {
            throw new IllegalArgumentException("Limit pinjaman tidak mencukupi");
        }

        this.id = id;
        this.borrower = borrower;
        this.amount = amount;
        this.tenor = tenor;
        this.purpose = purpose;
        this.createdAt = LocalDateTime.now();

        this.status = LoanStatus.DRAFT;
        this.stateBehavior = new DraftState();

        this.borrower.decreaseLimit(amount);
    }

    public String getId() {
        return id;
    }

    public Borrower getBorrower() {
        return borrower;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public int getTenor() {
        return tenor;
    }

    public String getPurpose() {
        return purpose;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void disburse() {
        this.stateBehavior.disburse(this);
    }


    public void submit() {
        this.stateBehavior.submit(this);
    }

    public void review() {
        this.stateBehavior.review(this);
    }

    public void approve() {
        this.stateBehavior.approve(this);
    }

    public void reject() {
        this.stateBehavior.reject(this);
    }

    public void updateState(LoanState newState, LoanStatus newStatus) {
        this.stateBehavior = newState;
        this.status = newStatus;
    }

}