package com.p2plending.domain.loan;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class LoanTest {

    @Test
    void createLoan_WithinLimit_SuccessAndStatusIsDraft() {
        Borrower borrower = new Borrower("B001", new BigDecimal("10000000"));
        Loan loan = new Loan("L001", borrower, new BigDecimal("4000000"));
        
        assertEquals(LoanStatus.DRAFT, loan.getStatus(), "Status awal harus DRAFT");
    }

    @Test
    void createLoan_ExceedsLimit_ThrowsException() {
        Borrower borrower = new Borrower("B002", new BigDecimal("5000000"));
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Loan("L002", borrower, new BigDecimal("8000000"));
        });
        assertEquals("Limit pinjaman tidak mencukupi", exception.getMessage());
    }

    @Test
    void submitLoan_FromDraft_ChangesToPending() {
        Borrower borrower = new Borrower("B001", new BigDecimal("10000000"));
        Loan loan = new Loan("L001", borrower, new BigDecimal("4000000")); 
        
        loan.submit(); // Method ini akan memicu State Pattern nantinya
        
        assertEquals(LoanStatus.PENDING, loan.getStatus());
    }
}