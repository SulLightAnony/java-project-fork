package com.p2plending.service;

import com.p2plending.domain.funding.Funding;
import com.p2plending.domain.loan.Loan;
import com.p2plending.notification.FundingEventPublisher;
import com.p2plending.repository.FundingRepository;
import com.p2plending.repository.LoanRepository;

import java.math.BigDecimal;

public class FundingService {

    private final LoanRepository loanRepository;
    private final FundingRepository fundingRepository;
    private final FundingEventPublisher eventPublisher;

    public FundingService(LoanRepository loanRepository,
                          FundingRepository fundingRepository,
                          FundingEventPublisher eventPublisher) {
        this.loanRepository = loanRepository;
        this.fundingRepository = fundingRepository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Lender memberikan kontribusi dana untuk pinjaman tertentu.
     *
     * @param loanId   ID pinjaman yang ingin didanai
     * @param lenderId ID lender yang memberikan kontribusi
     * @param amount   Jumlah dana yang dikontribusikan
     */
    public void contribute(String loanId, String lenderId, BigDecimal amount) {
        // 1. Pastikan pinjaman ada dan sudah dalam status PENDING
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Pinjaman tidak ditemukan: " + loanId));

        // 2. Ambil atau buat objek Funding untuk pinjaman ini
        Funding funding = fundingRepository.findByLoanId(loanId)
                .orElseGet(() -> new Funding(loanId, loan.getAmount()));

        // 3. Delegasikan logika kontribusi ke domain object
        funding.addContribution(lenderId, amount);

        // 4. Simpan perubahan state funding
        fundingRepository.save(funding);

        // 5. Jika sudah fully funded, publish event notifikasi
        if (funding.isFullyFunded()) {
            eventPublisher.publishFundingCompleted(loanId);
        }
    }
}
