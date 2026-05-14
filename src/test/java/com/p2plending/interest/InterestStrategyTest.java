package com.p2plending.interest;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import com.p2plending.interest.FloatingInterestStrategy;
import com.p2plending.interest.SyariahStrategy;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InterestStrategyTest {

    @Test
    void testFixedInterestStrategy() {
        // Arrange: Bunga tetap 10% (0.10)
        InterestStrategy strategy = new FixedInterestStrategy(new BigDecimal("0.10"));
        BigDecimal principal = new BigDecimal("10000000"); // 10 Juta

        // Act
        BigDecimal interest = strategy.calculate(principal, 12);

        // Assert: 10% dari 10 Juta = 1 Juta
        assertEquals(new BigDecimal("1000000.00"), interest);
    }

    @Test
    void testFloatingInterestStrategy() {
        // Arrange: Base rate 5% (0.05), Margin 3% (0.03) -> Total 8% (0.08)
        InterestStrategy strategy = new FloatingInterestStrategy(new BigDecimal("0.05"), new BigDecimal("0.03"));
        BigDecimal principal = new BigDecimal("10000000"); // 10 Juta

        // Act
        BigDecimal interest = strategy.calculate(principal, 12);

        // Assert: 8% dari 10 Juta = 800 Ribu
        assertEquals(new BigDecimal("800000.00"), interest);
    }

    @Test
    void testSyariahStrategy() {
        // Arrange: Margin bagi hasil 15% (0.15)
        InterestStrategy strategy = new SyariahStrategy(new BigDecimal("0.15"));
        BigDecimal principal = new BigDecimal("10000000"); // 10 Juta

        // Act
        BigDecimal profitMargin = strategy.calculate(principal, 12);

        // Assert: 15% dari 10 Juta = 1.5 Juta
        assertEquals(new BigDecimal("1500000.00"), profitMargin);
    }
}