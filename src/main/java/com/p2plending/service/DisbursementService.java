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
import java.math.RoundingMode;


/**
 * Service utama untuk mengurus pencairan dana (Disbursement).
 * Bertindak sebagai Orkestrator antar Domain, Repository, dan Strategy.
 */
public class DisbursementService {

    private final LoanRepository loanRepository;
    private final DisbursementRepository disbursementRepository;
    private final InterestStrategy interestStrategy;

    public DisbursementService(LoanRepository loanRepository, DisbursementRepository disbursementRepository) {
        this(loanRepository, disbursementRepository, null);
    }

    public DisbursementService(LoanRepository loanRepository,
                               DisbursementRepository disbursementRepository,
                               InterestStrategy interestStrategy) {
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

    public Disbursement disburseLoan(String loanId, InterestStrategy strategy) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found: " + loanId));

        BigDecimal interest = strategy.calculate(loan.getAmount(), loan.getTenor());
        BigDecimal totalRepayment = loan.getAmount().add(interest).setScale(2, RoundingMode.HALF_UP);
        BigDecimal monthlyInstallment = totalRepayment.divide(new BigDecimal(loan.getTenor()), 2, RoundingMode.HALF_UP);

        RepaymentSchedule schedule = new RepaymentSchedule(totalRepayment, monthlyInstallment);
        Disbursement disbursement = new Disbursement(loanId, loan.getAmount(), interest, schedule);

        loan.disburse();
        loan.setStatus(LoanStatus.ACTIVE);
        loan.setState(new ActiveState());

    public DisbursementService(LoanRepository loanRepository, DisbursementRepository disbursementRepository) {
        this.loanRepository = loanRepository;
        this.disbursementRepository = disbursementRepository;
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

        // 4. Buat jadwal cicilan
        RepaymentSchedule schedule = new RepaymentSchedule(principal, totalInterest, tenor);

        // 5. Ubah state Loan menjadi ACTIVE
        // WORKAROUND: Karena Orang 1 belum membuat getState() atau disburse() di Loan.java,
        // kita menggunakan setter secara langsung agar fitur Orang 4 tetap bisa jalan tanpa menyentuh kode Orang 1.
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
}
