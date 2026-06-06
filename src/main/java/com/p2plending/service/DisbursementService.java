package com.p2plending.service;

import com.p2plending.domain.disbursement.Disbursement;
import com.p2plending.domain.disbursement.RepaymentSchedule;
import com.p2plending.domain.loan.Loan;
import com.p2plending.interest.InterestStrategy;
import com.p2plending.repository.DisbursementRepository;
import com.p2plending.repository.LoanRepository;

import java.math.BigDecimal;

import com.p2plending.domain.loan.decorator.LoanCost;
import com.p2plending.domain.loan.decorator.SimpleLoan;
import com.p2plending.domain.loan.decorator.PlatformFeeDecorator;

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
        
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Pinjaman dengan ID " + loanId + " tidak ditemukan."));

        BigDecimal principal = loan.getAmount();
        int tenor = loan.getTenor();

        BigDecimal totalInterest = interestStrategy.calculate(principal, tenor);

        RepaymentSchedule schedule = new RepaymentSchedule(principal, totalInterest, tenor);

        loan.disburse();

        LoanCost totalCostStructure = new PlatformFeeDecorator(new SimpleLoan(principal), new BigDecimal("50000"));
        BigDecimal totalCost = totalCostStructure.getCost();
        System.out.println("Applying total cost structure: " + totalCostStructure.getDescription() + " = " + totalCost);

        Disbursement disbursement = new Disbursement(loanId, principal, schedule);

        loanRepository.save(loan);
        disbursementRepository.save(disbursement);

        return disbursement;
    }
}