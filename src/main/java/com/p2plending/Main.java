package com.p2plending;

import com.p2plending.domain.loan.Borrower;
import com.p2plending.domain.loan.Loan;
import com.p2plending.repository.impl.InMemoryLoanRepository;
import com.p2plending.repository.impl.InMemoryDisbursementRepository;
import com.p2plending.service.DisbursementService;
import com.p2plending.interest.SyariahStrategy;
import com.p2plending.domain.disbursement.Disbursement;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        logger.info("P2P Lending Core Engine Started.");
        
        InMemoryLoanRepository loanRepository = new InMemoryLoanRepository();
        InMemoryDisbursementRepository disbursementRepository = new InMemoryDisbursementRepository();
        SyariahStrategy syariahStrategy = new SyariahStrategy(new BigDecimal("0.12"));
        
        DisbursementService disbursementService = new DisbursementService(loanRepository, disbursementRepository, syariahStrategy);
        
        Borrower borrower = new Borrower("B-123", "Sulthan", 700, new BigDecimal("100000000"));
        Loan loan = new Loan("L-2026-X", borrower, new BigDecimal("50000000"), 12, "Modal Usaha");
        
        logger.log(Level.INFO, "Loan Initial State: {0}", loan.getStatus());
        loan.submit();
        logger.log(Level.INFO, "Loan State after Submit: {0}", loan.getStatus());
        loan.review();
        logger.log(Level.INFO, "Loan State after Review: {0}", loan.getStatus());
        loan.approve();
        logger.log(Level.INFO, "Loan State after Approve (Ready for Funding/Disbursement): {0}", loan.getStatus());
        
        loanRepository.save(loan);
        
        logger.log(Level.INFO, "Created Loan with Amount: {0}", loan.getAmount());
        
        logger.info("Disbursing Loan...");
        Disbursement disbursement = disbursementService.disburseLoan(loan.getId());
        
        logger.log(Level.INFO, "Disbursement schedule total amount to repay: {0}", disbursement.getRepaymentSchedule().getTotalAmountToRepay());
        logger.log(Level.INFO, "Disbursement schedule monthly installment: {0}", disbursement.getRepaymentSchedule().getMonthlyInstallment());
    }
}
