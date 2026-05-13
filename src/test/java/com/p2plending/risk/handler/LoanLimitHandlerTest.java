package com.p2plending.risk.handler;

import com.p2plending.risk.ValidationContext;
import com.p2plending.risk.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoanLimitHandlerTest {

    private LoanLimitHandler handler;

    @BeforeEach
    void setUp() {
        handler = new LoanLimitHandler();
    }

    @Test
    @DisplayName("Harus lolos jika jumlah pinjaman di bawah limit")
    void shouldPassWhenLoanAmountIsWithinLimit() {
        ValidationContext ctx = new ValidationContext(
            50_000_000, 700, 10_000_000, 2_000_000, 100_000_000
        );

        ValidationResult result = handler.handle(ctx);

        assertTrue(result.isApproved());
    }

    @Test
    @DisplayName("Harus reject jika jumlah pinjaman melebihi limit")
    void shouldRejectWhenLoanAmountExceedsLimit() {
        ValidationContext ctx = new ValidationContext(
            150_000_000, 700, 10_000_000, 2_000_000, 100_000_000
        );

        ValidationResult result = handler.handle(ctx);

        assertFalse(result.isApproved());
        assertEquals("Loan amount exceeds limit", result.getRejectionReason());
    }

    @Test
    @DisplayName("Harus reject jika jumlah pinjaman tepat sama dengan limit")
    void shouldPassWhenLoanAmountIsExactlyAtLimit() {
        ValidationContext ctx = new ValidationContext(
            100_000_000, 700, 10_000_000, 2_000_000, 100_000_000
        );

        ValidationResult result = handler.handle(ctx);

        assertTrue(result.isApproved());
    }
}