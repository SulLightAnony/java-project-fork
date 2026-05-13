package com.p2plending.service;

import com.p2plending.domain.loan.Loan;
import com.p2plending.repository.LoanRepository;
import com.p2plending.risk.RiskConstants;
import com.p2plending.risk.RiskEngine;
import com.p2plending.risk.ValidationContext;
import com.p2plending.risk.ValidationResult;

public class RiskEngineService {

    private final RiskEngine riskEngine;
    private final LoanRepository loanRepository;

    public RiskEngineService(RiskEngine riskEngine, LoanRepository loanRepository) {
        this.riskEngine = riskEngine;
        this.loanRepository = loanRepository;
    }

    public ValidationResult evaluate(Loan loan,
                                     double monthlyIncome, double existingMonthlyDebt) {
        loan.review();

        ValidationContext context = new ValidationContext(
            loan.getAmount().doubleValue(),
            loan.getBorrower().getCreditScore(),
            monthlyIncome,
            existingMonthlyDebt,
            RiskConstants.MAX_LOAN_LIMIT
        );

        ValidationResult result = riskEngine.evaluate(context);

        if (result.isApproved()) {
            loan.approve();
        } else {
            loan.reject();
        }

        loanRepository.save(loan);

        return result;
    }
}