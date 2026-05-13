package com.p2plending.domain.loan.state;

import com.p2plending.domain.loan.Loan;
import com.p2plending.domain.loan.LoanStatus;

public class DraftState implements LoanState {
    @Override
    public void submit(Loan context) {
        context.updateState(new PendingState(), LoanStatus.PENDING);
    }

    @Override
    public void review(Loan context) {
        throw new IllegalStateException("Cannot review a draft loan");
    }

    @Override
    public void approve(Loan context) {
        throw new IllegalStateException("Cannot approve a draft loan");
    }

    @Override
    public void reject(Loan context) {
        throw new IllegalStateException("Cannot reject a draft loan");
    }
}