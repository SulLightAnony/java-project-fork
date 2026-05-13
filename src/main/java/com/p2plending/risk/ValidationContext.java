package com.p2plending.risk;

public class ValidationContext {
    private final double requestedAmount;
    private final int creditScore;
    private final double monthlyIncome;
    private final double existingMonthlyDebt;
    private final double maxLoanLimit;

    public ValidationContext(double requestedAmount, int creditScore,
                             double monthlyIncome, double existingMonthlyDebt,
                             double maxLoanLimit) {
        this.requestedAmount = requestedAmount;
        this.creditScore = creditScore;
        this.monthlyIncome = monthlyIncome;
        this.existingMonthlyDebt = existingMonthlyDebt;
        this.maxLoanLimit = maxLoanLimit;
    }

    public double getRequestedAmount() { return requestedAmount; }
    public int getCreditScore() { return creditScore; }
    public double getMonthlyIncome() { return monthlyIncome; }
    public double getExistingMonthlyDebt() { return existingMonthlyDebt; }
    public double getMaxLoanLimit() { return maxLoanLimit; }
}