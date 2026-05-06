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

    public String applyForLoan(String borrowerId, BigDecimal amount) {
        // 1. Tarik data peminjam dari In-Memory Repository
        Borrower borrower = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new IllegalArgumentException("Borrower tidak ditemukan"));

        // 2. Buat ID Pinjaman
        String loanId = "L-" + UUID.randomUUID().toString().substring(0, 8);

        // 3. Eksekusi Domain Logic (Jika limit kurang, sistem akan melempar error otomatis dari dalam entitas Loan)
        Loan loan = new Loan(loanId, borrower, amount);

        // 4. Simpan mutasi data ke In-Memory Repository
        loanRepository.save(loan);
        borrowerRepository.save(borrower);

        return loanId;
    }
}