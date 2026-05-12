package com.p2plending.domain.loan.state;

import com.p2plending.domain.loan.Loan;

public class RejectedState implements LoanState {
    @Override
    public void submit(Loan context) {
        throw new IllegalStateException("Cannot submit a rejected loan");
    }

    @Override
    public void review(Loan context) {
        throw new IllegalStateException("Cannot review a rejected loan");
    }

    @Override
    public void approve(Loan context) {
        throw new IllegalStateException("Cannot approve a rejected loan");
    }

    @Override
    public void reject(Loan context) {
        throw new IllegalStateException("Loan is already rejected");
    }
}
