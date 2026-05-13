package com.p2plending.risk.handler;

import com.p2plending.risk.RiskConstants;
import com.p2plending.risk.ValidationContext;
import com.p2plending.risk.ValidationResult;

public class CreditScoreHandler extends ValidationHandler {

    @Override
    public ValidationResult handle(ValidationContext context) {
        if (context.getCreditScore() < RiskConstants.MIN_CREDIT_SCORE) {
            return ValidationResult.reject("Credit score below minimum");
        }
        return passToNext(context);
    }
}