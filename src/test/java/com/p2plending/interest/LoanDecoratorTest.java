package com.p2plending.interest;

import com.p2plending.domain.loan.decorator.LoanCost;
import com.p2plending.domain.loan.decorator.PlatformFeeDecorator;
import com.p2plending.domain.loan.decorator.SimpleLoan;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoanDecoratorTest {

    @Test
    public void shouldReturnBasePrincipalForSimpleLoan() {
        BigDecimal principal = new BigDecimal("1000000.00");
        LoanCost loanCost = new SimpleLoan(principal);

        assertEquals(principal.setScale(2, RoundingMode.HALF_UP), loanCost.getCost());
        assertEquals("Pokok Pinjaman", loanCost.getDescription());
    }

    @Test
    public void shouldAccumulateCostAndDescriptionWithPlatformFee() {
        BigDecimal principal = new BigDecimal("1000000.00");
        BigDecimal fee = new BigDecimal("50000.00");
        
        LoanCost loanCost = new PlatformFeeDecorator(new SimpleLoan(principal), fee);

        BigDecimal expectedTotal = principal.add(fee).setScale(2, RoundingMode.HALF_UP);
        assertEquals(expectedTotal, loanCost.getCost());
        assertEquals("Pokok Pinjaman + Biaya Platform", loanCost.getDescription());
    }
}
