package com.p2plending;

import com.p2plending.domain.loan.Borrower;
import com.p2plending.domain.loan.Loan;
import com.p2plending.domain.loan.LoanStatus;
import com.p2plending.domain.loan.state.DraftState;
import com.p2plending.repository.impl.InMemoryLoanRepository;
import com.p2plending.repository.impl.InMemoryDisbursementRepository;
import com.p2plending.service.DisbursementService;
import com.p2plending.interest.SyariahStrategy;
import com.p2plending.domain.disbursement.Disbursement;
import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        System.out.println("P2P Lending Core Engine Started.");
        
        InMemoryLoanRepository loanRepository = new InMemoryLoanRepository();
        InMemoryDisbursementRepository disbursementRepository = new InMemoryDisbursementRepository();
        SyariahStrategy syariahStrategy = new SyariahStrategy(new BigDecimal("0.12"));
        
        DisbursementService disbursementService = new DisbursementService(loanRepository, disbursementRepository, syariahStrategy);
        
        Borrower borrower = new Borrower("B-123", "Sulthan", 700, new BigDecimal("100000000"));
        Loan loan = new Loan("L-2026-X", borrower, new BigDecimal("50000000"), 12, "Modal Usaha");
        
        System.out.println("Loan Initial State: " + loan.getStatus());
        loan.submit();
        System.out.println("Loan State after Submit: " + loan.getStatus());
        loan.review();
        System.out.println("Loan State after Review: " + loan.getStatus());
        loan.approve();
        System.out.println("Loan State after Approve (Ready for Funding/Disbursement): " + loan.getStatus());
        
        loanRepository.save(loan);
        
        System.out.println("Created Loan with Amount: " + loan.getAmount());
        
        System.out.println("Disbursing Loan...");
        Disbursement disbursement = disbursementService.disburseLoan(loan.getId());
        
        System.out.println("Disbursement schedule total amount to repay: " + disbursement.getRepaymentSchedule().getTotalAmountToRepay());
        System.out.println("Disbursement schedule monthly installment: " + disbursement.getRepaymentSchedule().getMonthlyInstallment());
    }
}
