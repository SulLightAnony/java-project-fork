package com.p2plending.domain.funding;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Funding {

    private final String loanId;
    private final BigDecimal targetAmount;
    private final List<LenderContribution> contributions = new ArrayList<>();

    public Funding(String loanId, BigDecimal targetAmount) {
        this.loanId = loanId;
        this.targetAmount = targetAmount;
    }

    public void addContribution(String lenderId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Jumlah kontribusi harus lebih dari nol");
        }

        BigDecimal remaining = targetAmount.subtract(getTotalFunded());
        if (amount.compareTo(remaining) > 0) {
            throw new IllegalArgumentException("Jumlah kontribusi melebihi sisa dana yang dibutuhkan");
        }

        contributions.add(new LenderContribution(lenderId, amount));
    }

    public BigDecimal getTotalFunded() {
        return contributions.stream()
                .map(LenderContribution::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getProgressPercentage() {
        return getTotalFunded()
                .multiply(new BigDecimal("100"))
                .divide(targetAmount, 2, RoundingMode.DOWN)
                .stripTrailingZeros();
    }

    public boolean isFullyFunded() {
        return getTotalFunded().compareTo(targetAmount) >= 0;
    }

    public List<LenderContribution> getContributions() {
        return Collections.unmodifiableList(contributions);
    }

    public Set<String> getContributingLenderIds() {
        return contributions.stream()
                .map(LenderContribution::getLenderId)
                .collect(Collectors.toSet());
    }

    public BigDecimal getTotalContributionBy(String lenderId) {
        return contributions.stream()
                .filter(c -> c.getLenderId().equals(lenderId))
                .map(LenderContribution::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public String getLoanId() {
        return loanId;
    }
}