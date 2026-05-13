package com.p2plending.risk.handler;

import com.p2plending.risk.ValidationContext;
import com.p2plending.risk.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DebtRatioHandlerTest {

    private DebtRatioHandler handler;

    @BeforeEach
    void setUp() {
        handler = new DebtRatioHandler();
    }

    @Test
    @DisplayName("Harus lolos jika DTI ratio di bawah 43%")
    void shouldPassWhenDTIIsAcceptable() {
        // income 10jt, existing debt 2jt → DTI = 2/10 = 20%
        ValidationContext ctx = new ValidationContext(
            50_000_000, 700, 10_000_000, 2_000_000, 100_000_000
        );

        ValidationResult result = handler.handle(ctx);

        assertTrue(result.isApproved());
    }

    @Test
    @DisplayName("Harus reject jika DTI ratio melebihi 43%")
    void shouldRejectWhenDTIExceedsLimit() {
        // income 10jt, existing debt 5jt → DTI = 5/10 = 50%
        ValidationContext ctx = new ValidationContext(
            50_000_000, 700, 10_000_000, 5_000_000, 100_000_000
        );

        ValidationResult result = handler.handle(ctx);

        assertFalse(result.isApproved());
        assertEquals("DTI ratio exceeds limit", result.getRejectionReason());
    }

    @Test
    @DisplayName("Harus reject jika income nol untuk hindari division by zero")
    void shouldRejectWhenIncomeIsZero() {
        ValidationContext ctx = new ValidationContext(
            50_000_000, 700, 0, 0, 100_000_000
        );

        ValidationResult result = handler.handle(ctx);

        assertFalse(result.isApproved());
        assertEquals("Monthly income must be greater than zero", result.getRejectionReason());
    }
}