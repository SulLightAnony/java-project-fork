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
        loanRepository.save(loan);
        disbursementRepository.save(disbursement);

        return disbursement;
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> 12966222ce23a6d3b9138e9079d137d30f3e4c72
