package com.p2plending.risk;

import com.p2plending.risk.handler.CreditScoreHandler;
import com.p2plending.risk.handler.DebtRatioHandler;
import com.p2plending.risk.handler.LoanLimitHandler;
import com.p2plending.risk.handler.ValidationHandler;

public class RiskEngineImpl implements RiskEngine {

    private final ValidationHandler chain;

    public RiskEngineImpl() {
        CreditScoreHandler creditScore = new CreditScoreHandler();
        DebtRatioHandler debtRatio = new DebtRatioHandler();
        LoanLimitHandler loanLimit = new LoanLimitHandler();

        // Rangkai chain: CreditScore → DebtRatio → LoanLimit
        creditScore.setNext(debtRatio).setNext(loanLimit);

        this.chain = creditScore;
    }

    @Override
    public ValidationResult evaluate(ValidationContext context) {
        return chain.handle(context);
    }
}