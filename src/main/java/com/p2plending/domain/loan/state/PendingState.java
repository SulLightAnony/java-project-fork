package com.p2plending.domain.loan.state;

import com.p2plending.domain.loan.Loan;

public class PendingState implements LoanState {
    @Override
    public void submit(Loan context) {
        throw new IllegalStateException("Pinjaman sudah disubmit dan sedang menunggu evaluasi.");
    }
}