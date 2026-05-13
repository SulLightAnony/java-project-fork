package com.p2plending.domain.loan.state;

import com.p2plending.domain.loan.Loan;
import com.p2plending.domain.loan.LoanStatus;

public class FundingState implements LoanState {
    @Override
    public void submit(Loan context) {
        throw new IllegalStateException("Loan already in funding");
    }

    @Override
    public void review(Loan context) {
        throw new IllegalStateException("Loan already reviewed and in funding");
    }

    @Override
    public void approve(Loan context) {
        throw new IllegalStateException("Loan already approved");
    }

    @Override
    public void reject(Loan context) {
        throw new IllegalStateException("Cannot reject a loan that is already in funding");
    }
}
