package com.p2plending.domain.loan.state;

import com.p2plending.domain.loan.Loan;
import com.p2plending.domain.loan.LoanStatus;

public class ReviewingState implements LoanState {
    @Override
    public void submit(Loan context) {
        throw new IllegalStateException("Loan already submitted and is under review");
    }

    @Override
    public void review(Loan context) {
        throw new IllegalStateException("Loan is already being reviewed");
    }

    @Override
    public void approve(Loan context) {
        context.updateState(new FundingState(), LoanStatus.FUNDING);
    }

    @Override
    public void reject(Loan context) {
        context.updateState(new RejectedState(), LoanStatus.REJECTED);
    }

    @Override
    public void disburse(Loan context) {
        throw new IllegalStateException("Cannot disburse a reviewing loan");
    }
}
