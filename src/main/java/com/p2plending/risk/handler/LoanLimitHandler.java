package com.p2plending.risk.handler;

import com.p2plending.risk.RiskConstants;
import com.p2plending.risk.ValidationContext;
import com.p2plending.risk.ValidationResult;

public class LoanLimitHandler extends ValidationHandler {

    @Override
    public ValidationResult handle(ValidationContext context) {
        if (context.getRequestedAmount() > context.getMaxLoanLimit()) {
            return ValidationResult.reject("Loan amount exceeds limit");
        }
        return passToNext(context);
    }
}