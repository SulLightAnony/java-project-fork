package com.p2plending.domain.loan.state;

import com.p2plending.domain.loan.Loan;
import com.p2plending.domain.loan.LoanStatus;

public class DraftState implements LoanState {
    @Override
    public void submit(Loan context) {
        // Mengubah status logis dan status data secara bersamaan
        context.updateState(new PendingState(), LoanStatus.PENDING);
    }
}