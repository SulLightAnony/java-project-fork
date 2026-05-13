package com.p2plending.risk.handler;

import com.p2plending.risk.ValidationContext;
import com.p2plending.risk.ValidationResult;

public class DebtRatioHandler extends ValidationHandler {

    private static final double MAXIMUM_DTI_RATIO = 0.43;

    @Override
    public ValidationResult handle(ValidationContext context) {
        if (context.getMonthlyIncome() <= 0) {
            return ValidationResult.reject("Monthly income must be greater than zero");
        }

        double dti = context.getExistingMonthlyDebt() / context.getMonthlyIncome();

        if (dti > MAXIMUM_DTI_RATIO) {
            return ValidationResult.reject("DTI ratio exceeds limit");
        }

        return passToNext(context);
    }
}