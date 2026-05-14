package com.p2plending.risk.handler;

import com.p2plending.risk.RiskConstants;
import com.p2plending.risk.ValidationContext;
import com.p2plending.risk.ValidationResult;

public class DebtRatioHandler extends ValidationHandler {

    @Override
    public ValidationResult handle(ValidationContext context) {
        if (context.getMonthlyIncome() <= 0) {
            return ValidationResult.reject("Monthly income must be greater than zero");
        }
        if (exceedsMaximumDTI(context)) {
            return ValidationResult.reject("DTI ratio exceeds limit");
        }
        return passToNext(context);
    }

    private boolean exceedsMaximumDTI(ValidationContext context) {
        double dti = context.getExistingMonthlyDebt() / context.getMonthlyIncome();
        return dti > RiskConstants.MAX_DTI_RATIO;
    }
}