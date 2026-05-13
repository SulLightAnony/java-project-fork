package com.p2plending.risk.handler;

import com.p2plending.risk.ValidationContext;
import com.p2plending.risk.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreditScoreHandlerTest {

    private CreditScoreHandler handler;

    @BeforeEach
    void setUp() {
        handler = new CreditScoreHandler();
    }

    @Test
    @DisplayName("Harus lolos jika credit score di atas minimum (600)")
    void shouldPassWhenCreditScoreIsAboveMinimum() {
        ValidationContext ctx = new ValidationContext(
            50_000_000, 700, 10_000_000, 2_000_000, 100_000_000
        );

        ValidationResult result = handler.handle(ctx);

        assertTrue(result.isApproved());
    }

    @Test
    @DisplayName("Harus reject jika credit score di bawah minimum (600)")
    void shouldRejectWhenCreditScoreIsBelowMinimum() {
        ValidationContext ctx = new ValidationContext(
            50_000_000, 400, 10_000_000, 2_000_000, 100_000_000
        );

        ValidationResult result = handler.handle(ctx);

        assertFalse(result.isApproved());
        assertEquals("Credit score below minimum", result.getRejectionReason());
    }

    @Test
    @DisplayName("Harus reject jika credit score tepat di batas bawah (599)")
    void shouldRejectWhenCreditScoreIsAtBoundary() {
        ValidationContext ctx = new ValidationContext(
            50_000_000, 599, 10_000_000, 2_000_000, 100_000_000
        );

        ValidationResult result = handler.handle(ctx);

        assertFalse(result.isApproved());
    }

    @Test
    @DisplayName("Harus lanjut ke handler berikutnya jika lolos")
    void shouldPassToNextHandlerWhenApproved() {
        ValidationContext ctx = new ValidationContext(
            50_000_000, 750, 10_000_000, 2_000_000, 100_000_000
        );

        // next handler yang selalu reject — untuk buktikan chain berjalan
        ValidationHandler alwaysReject = new ValidationHandler() {
            @Override
            public ValidationResult handle(ValidationContext context) {
                return ValidationResult.reject("Next handler triggered");
            }
        };

        handler.setNext(alwaysReject);
        ValidationResult result = handler.handle(ctx);

        assertFalse(result.isApproved());
        assertEquals("Next handler triggered", result.getRejectionReason());
    }
}