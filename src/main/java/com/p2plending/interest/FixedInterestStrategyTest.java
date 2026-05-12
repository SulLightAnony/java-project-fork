package com.p2plending.interest;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FixedInterestStrategyTest {

    @Test
    void shouldCalculateFixedInterestCorrectly() {
        FixedInterestStrategy strategy = new FixedInterestStrategy(new BigDecimal("0.10"));
        BigDecimal principal = new BigDecimal("10000000"); 
        int tenorInMonths = 12;

        BigDecimal totalInterest = strategy.calculate(principal, tenorInMonths);
        assertEquals(new BigDecimal("1000000.00"), totalInterest);
    }
}