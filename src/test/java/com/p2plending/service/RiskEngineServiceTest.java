package com.p2plending.service;

import com.p2plending.domain.loan.Borrower;
import com.p2plending.domain.loan.Loan;
import com.p2plending.repository.LoanRepository;
import com.p2plending.risk.RiskEngine;
import com.p2plending.risk.ValidationContext;
import com.p2plending.risk.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RiskEngineServiceTest {

    @Mock private RiskEngine riskEngine;
    @Mock private LoanRepository loanRepository;
    @Mock private Loan loan;
    @Mock private Borrower borrower;

    private RiskEngineService service;

    @BeforeEach
    void setUp() {
        service = new RiskEngineService(riskEngine, loanRepository);

        // Setup default mock Loan & Borrower
        when(loan.getBorrower()).thenReturn(borrower);
        when(loan.getAmount()).thenReturn(new BigDecimal("20000000"));
        when(borrower.getCreditScore()).thenReturn(700);
    }

    @Test
    @DisplayName("Harus approve dan panggil loan.approve() jika semua validasi lolos")
    void shouldApproveWhenAllValidationPass() {
        when(riskEngine.evaluate(any(ValidationContext.class)))
            .thenReturn(ValidationResult.approve());

        ValidationResult result = service.evaluate(loan, 10_000_000, 2_000_000);

        assertTrue(result.isApproved());
        verify(loan).review();
        verify(loan).approve();
        verify(loan, never()).reject();
        verify(loanRepository).save(loan);
    }

    @Test
    @DisplayName("Harus reject dan panggil loan.reject() jika credit score tidak memenuhi syarat")
    void shouldRejectWhenCreditScoreFails() {
        when(borrower.getCreditScore()).thenReturn(400);
        when(riskEngine.evaluate(any(ValidationContext.class)))
            .thenReturn(ValidationResult.reject("Credit score below minimum"));

        ValidationResult result = service.evaluate(loan, 10_000_000, 2_000_000);

        assertFalse(result.isApproved());
        assertEquals("Credit score below minimum", result.getRejectionReason());
        verify(loan).review();
        verify(loan).reject();
        verify(loan, never()).approve();
        verify(loanRepository).save(loan);
    }

    @Test
    @DisplayName("Harus reject jika DTI ratio terlalu tinggi")
    void shouldRejectWhenDTIFails() {
        when(riskEngine.evaluate(any(ValidationContext.class)))
            .thenReturn(ValidationResult.reject("DTI ratio exceeds limit"));

        ValidationResult result = service.evaluate(loan, 10_000_000, 5_000_000);

        assertFalse(result.isApproved());
        assertEquals("DTI ratio exceeds limit", result.getRejectionReason());
        verify(loan).review();
        verify(loan).reject();
        verify(loanRepository).save(loan);
    }

    @Test
    @DisplayName("Harus reject jika pinjaman melebihi limit")
    void shouldRejectWhenLoanLimitFails() {
        when(loan.getAmount()).thenReturn(new BigDecimal("75000000"));
        when(riskEngine.evaluate(any(ValidationContext.class)))
            .thenReturn(ValidationResult.reject("Loan amount exceeds limit"));

        ValidationResult result = service.evaluate(loan, 10_000_000, 2_000_000);

        assertFalse(result.isApproved());
        assertEquals("Loan amount exceeds limit", result.getRejectionReason());
        verify(loan).review();
        verify(loan).reject();
        verify(loanRepository).save(loan);
    }
}