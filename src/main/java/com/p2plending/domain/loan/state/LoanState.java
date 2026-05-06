package com.p2plending.domain.loan.state;

import com.p2plending.domain.loan.Loan;

public interface LoanState {
    void submit(Loan context);
}