package com.p2plending.service;

import com.p2plending.domain.disbursement.Disbursement;
import com.p2plending.domain.disbursement.RepaymentSchedule;
import com.p2plending.domain.loan.Loan;
import com.p2plending.domain.loan.LoanStatus;
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

    // Dependency Injection (Memasukkan repository dari luar)
    public DisbursementService(LoanRepository loanRepository, DisbursementRepository disbursementRepository) {
        this.loanRepository = loanRepository;
        this.disbursementRepository = disbursementRepository;
    }

    /**
     * Memproses pencairan dana untuk sebuah pinjaman.
     * @param loanId ID pinjaman yang akan dicairkan
     * @param interestStrategy Strategi kalkulasi bunga (Fixed/Floating/Syariah)
     * @return Entitas Disbursement sebagai bukti pencairan
     */
    public Disbursement disburseLoan(String loanId, InterestStrategy interestStrategy) {
        
        // 1. Validasi & Ambil data Loan
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Pinjaman dengan ID " + loanId + " tidak ditemukan."));

        // 2. Ambil data dasar pinjaman (asumsi getter ini ada di class Loan milik Orang 1)
        BigDecimal principal = loan.getAmount();
        int tenor = loan.getTenor();

        // 3. Kalkulasi Bunga menggunakan GoF Strategy Pattern
        BigDecimal totalInterest = interestStrategy.calculate(principal, tenor);

        // 4. Buat jadwal cicilan (Repayment Schedule)
        RepaymentSchedule schedule = new RepaymentSchedule(principal, totalInterest, tenor);

        // 5. Ubah state Loan menjadi ACTIVE (State Pattern trigger)
        // Ini akan memicu metode disburse() di FundingState yang sudah kita modifikasi
        loan.setLoanStatus(LoanStatus.DISBURSED);

        // 6. Catat bukti pencairan
        Disbursement disbursement = new Disbursement(loanId, principal, schedule);

        // 7. Simpan seluruh perubahan ke Repository
        loanRepository.save(loan);
        disbursementRepository.save(disbursement);

        return disbursement;
    }
}