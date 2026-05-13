package com.p2plending.service;

import com.p2plending.risk.RiskEngine;
import com.p2plending.risk.ValidationContext;
import com.p2plending.risk.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RiskEngineServiceTest {

    @Mock
    private RiskEngine riskEngine;

    private RiskEngineService service;

    @BeforeEach
    void setUp() {
        service = new RiskEngineService(riskEngine);
    }

    @Test
    @DisplayName("Harus approve jika risk engine lolos semua validasi")
    void shouldApproveWhenAllValidationPass() {
        when(riskEngine.evaluate(any(ValidationContext.class)))
            .thenReturn(ValidationResult.approve());

        ValidationResult result = service.evaluate(
            50_000_000, 700, 10_000_000, 2_000_000, 100_000_000
        );

        assertTrue(result.isApproved());
        verify(riskEngine, times(1)).evaluate(any(ValidationContext.class));
    }

    @Test
    @DisplayName("Harus reject jika credit score tidak memenuhi syarat")
    void shouldRejectWhenCreditScoreFails() {
        when(riskEngine.evaluate(any(ValidationContext.class)))
            .thenReturn(ValidationResult.reject("Credit score below minimum"));

        ValidationResult result = service.evaluate(
            50_000_000, 400, 10_000_000, 2_000_000, 100_000_000
        );

        assertFalse(result.isApproved());
        assertEquals("Credit score below minimum", result.getRejectionReason());
    }

    @Test
    @DisplayName("Harus reject jika DTI ratio terlalu tinggi")
    void shouldRejectWhenDTIFails() {
        when(riskEngine.evaluate(any(ValidationContext.class)))
            .thenReturn(ValidationResult.reject("DTI ratio exceeds limit"));

        ValidationResult result = service.evaluate(
            50_000_000, 700, 10_000_000, 5_000_000, 100_000_000
        );

        assertFalse(result.isApproved());
        assertEquals("DTI ratio exceeds limit", result.getRejectionReason());
    }

    @Test
    @DisplayName("Harus reject jika pinjaman melebihi limit")
    void shouldRejectWhenLoanLimitFails() {
        when(riskEngine.evaluate(any(ValidationContext.class)))
            .thenReturn(ValidationResult.reject("Loan amount exceeds limit"));

        ValidationResult result = service.evaluate(
            150_000_000, 700, 10_000_000, 2_000_000, 100_000_000
        );

        assertFalse(result.isApproved());
        assertEquals("Loan amount exceeds limit", result.getRejectionReason());
    }
}