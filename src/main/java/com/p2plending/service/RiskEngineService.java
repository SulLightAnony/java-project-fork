package com.p2plending.service;

import com.p2plending.risk.RiskEngine;
import com.p2plending.risk.ValidationContext;
import com.p2plending.risk.ValidationResult;

public class RiskEngineService {

    private final RiskEngine riskEngine;

    public RiskEngineService(RiskEngine riskEngine) {
        this.riskEngine = riskEngine;
    }

    public ValidationResult evaluate(double requestedAmount, int creditScore,
                                     double monthlyIncome, double existingMonthlyDebt,
                                     double maxLoanLimit) {
        ValidationContext context = new ValidationContext(
            requestedAmount, creditScore, monthlyIncome, existingMonthlyDebt, maxLoanLimit
        );
        return riskEngine.evaluate(context);
    }
}