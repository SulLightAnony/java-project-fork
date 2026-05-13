package com.p2plending.risk.handler;

import com.p2plending.risk.ValidationContext;
import com.p2plending.risk.ValidationResult;

public class CreditScoreHandler extends ValidationHandler {

    private static final int MINIMUM_CREDIT_SCORE = 600;

    @Override
    public ValidationResult handle(ValidationContext context) {
        if (context.getCreditScore() < MINIMUM_CREDIT_SCORE) {
            return ValidationResult.reject("Credit score below minimum");
        }
        return passToNext(context);
    }
}