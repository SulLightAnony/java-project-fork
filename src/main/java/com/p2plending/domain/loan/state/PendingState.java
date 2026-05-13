package com.p2plending.domain.loan.state;

import com.p2plending.domain.loan.Loan;
import com.p2plending.domain.loan.LoanStatus;

public class PendingState implements LoanState {
    @Override
    public void submit(Loan context) {
        throw new IllegalStateException("Loan already submitted");
    }

    @Override
    public void review(Loan context) {
        context.updateState(new ReviewingState(), LoanStatus.REVIEWING);
    }

    @Override
    public void approve(Loan context) {
        throw new IllegalStateException("Cannot approve a pending loan, it must be reviewed first");
    }

    @Override
    public void reject(Loan context) {
        throw new IllegalStateException("Cannot reject a pending loan, it must be reviewed first");
    }
}