package com.p2plending.interest;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InterestStrategyTest {

    @Test
    void testFixedInterestStrategy() {
        InterestStrategy strategy = new FixedInterestStrategy(new BigDecimal("0.10"));
        BigDecimal principal = new BigDecimal("10000000");

        BigDecimal interest = strategy.calculate(principal, 12);
        assertEquals(new BigDecimal("1000000.00"), interest);
    }

    @Test
    void testFloatingInterestStrategy() {
        // Base rate 5% + Margin 3% = 8%
        InterestStrategy strategy = new FloatingInterestStrategy(new BigDecimal("0.05"), new BigDecimal("0.03"));
        BigDecimal principal = new BigDecimal("10000000");

        BigDecimal interest = strategy.calculate(principal, 12);
        assertEquals(new BigDecimal("800000.00"), interest);
    }

    @Test
    void testSyariahStrategy() {
        // Margin bagi hasil 15%
        InterestStrategy strategy = new SyariahStrategy(new BigDecimal("0.15"));
        BigDecimal principal = new BigDecimal("10000000");

        BigDecimal profitMargin = strategy.calculate(principal, 12);
        assertEquals(new BigDecimal("1500000.00"), profitMargin);
    }
}