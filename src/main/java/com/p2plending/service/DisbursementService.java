package com.p2plending.service;

import com.p2plending.domain.disbursement.Disbursement;
import com.p2plending.domain.disbursement.RepaymentSchedule;
import com.p2plending.domain.loan.Loan;
import com.p2plending.domain.loan.LoanStatus;
import com.p2plending.domain.loan.state.ActiveState;
import com.p2plending.interest.InterestStrategy;
import com.p2plending.repository.DisbursementRepository;
import com.p2plending.repository.LoanRepository;

import java.math.BigDecimal;

/**
 * Service utama untuk mengurus pencairan dana (Disbursement).
 * Bertindak sebagai Orkestrator antar Domain, Repository, dan Strategy.
 */
public class DisbursementService {
    private final LoanRepository loanRepository;
    private final DisbursementRepository disbursementRepository;
    private final InterestStrategy interestStrategy;

    public DisbursementService(LoanRepository loanRepository, DisbursementRepository disbursementRepository, InterestStrategy interestStrategy) {
        this.loanRepository = loanRepository;
        this.disbursementRepository = disbursementRepository;
        this.interestStrategy = interestStrategy;
    }

    public Disbursement disburseLoan(String loanId) {
        if (interestStrategy == null) {
            throw new IllegalStateException("InterestStrategy must be provided");
        }
        return disburseLoan(loanId, interestStrategy);
    }

    public Disbursement disburseLoan(String loanId, InterestStrategy interestStrategy) {
        // 1. Validasi & Ambil data Loan
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Pinjaman dengan ID " + loanId + " tidak ditemukan."));

        // 2. Ambil data dasar pinjaman
        BigDecimal principal = loan.getAmount();
        int tenor = loan.getTenor();

        // 3. Kalkulasi Bunga
        BigDecimal totalInterest = interestStrategy.calculate(principal, tenor);

        // 4. Buat jadwal cicilan (sesuai konstruktor RepaymentSchedule yang tersedia)
        RepaymentSchedule schedule = new RepaymentSchedule(principal, totalInterest, tenor);

        // 5. Ubah state Loan menjadi DISBURSED / ACTIVE
        loan.setLoanStatus(LoanStatus.DISBURSED);
        loan.setLoanState(new ActiveState());

        // 6. Catat bukti pencairan
        Disbursement disbursement = new Disbursement(loanId, principal, schedule);

        // 7. Simpan perubahan ke Repository
        loanRepository.save(loan);
        disbursementRepository.save(disbursement);

        return disbursement;
    }
}