package com.p2plending.service;

import com.p2plending.domain.loan.Borrower;
import com.p2plending.domain.loan.Loan;
import com.p2plending.repository.BorrowerRepository;
import com.p2plending.repository.LoanRepository;

import java.math.BigDecimal;
import java.util.UUID;

public class LoanApplicationService {

    private final BorrowerRepository borrowerRepository;
    private final LoanRepository loanRepository;

    public LoanApplicationService(BorrowerRepository borrowerRepository, LoanRepository loanRepository) {
        this.borrowerRepository = borrowerRepository;
        this.loanRepository = loanRepository;
    }

    public String apply(String borrowerId, BigDecimal amount, int tenor, String purpose) {
        Borrower borrower = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new IllegalArgumentException("Borrower not found"));

        if (!borrower.isEligible()) {
            throw new IllegalArgumentException("Borrower is not eligible for a loan");
        }

        if (!borrower.canBorrow(amount)) {
            throw new IllegalArgumentException("Insufficient borrowing limit");
        }

        String loanId = UUID.randomUUID().toString();
        Loan loan = new Loan(loanId, borrower, amount, tenor, purpose);

        loanRepository.save(loan);
        borrowerRepository.save(borrower);

        return loanId;
    }
}