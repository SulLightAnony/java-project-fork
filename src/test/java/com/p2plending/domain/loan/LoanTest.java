package com.p2plending.domain.loan;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.function.Executable;

class LoanTest {

    @Test
    void createLoan_WithinLimit_SuccessAndStatusIsDraft() {
        Borrower borrower = new Borrower("B001", "Ismail", 600, new BigDecimal("10000000"));
        Loan loan = new Loan("L001", borrower, new BigDecimal("4000000"), 12, "Modal Usaha");
        
        assertEquals(LoanStatus.DRAFT, loan.getStatus());
        assertEquals(new BigDecimal("6000000"), borrower.getRemainingLimit());
    }

    @Test
    void createLoan_ExceedsLimit_ThrowsException() {
        Borrower borrower = new Borrower("B002", "Basyir", 600, new BigDecimal("5000000"));

        Executable executable = () ->
            new Loan("L002", borrower, new BigDecimal("8000000"), 12, "Renovasi");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, executable);
        assertEquals("Limit pinjaman tidak mencukupi", exception.getMessage());
    }

    @Test
    void stateTransition_SuccessFlow() {
        Borrower borrower = new Borrower("B001", "Ismail", 600, new BigDecimal("10000000"));
        Loan loan = new Loan("L001", borrower, new BigDecimal("4000000"), 12, "Modal Usaha");
        
        assertEquals(LoanStatus.DRAFT, loan.getStatus());
        
        loan.submit();
        assertEquals(LoanStatus.PENDING, loan.getStatus());
        
        loan.review();
        assertEquals(LoanStatus.REVIEWING, loan.getStatus());
        
        loan.approve();
        assertEquals(LoanStatus.FUNDING, loan.getStatus());
    }

    @Test
    void stateTransition_RejectionFlow() {
        Borrower borrower = new Borrower("B001", "Ismail", 600, new BigDecimal("10000000"));
        Loan loan = new Loan("L001", borrower, new BigDecimal("4000000"), 12, "Modal Usaha");
        
        loan.submit();
        loan.review();
        loan.reject();
        
        assertEquals(LoanStatus.REJECTED, loan.getStatus());
    }

    @Test
    void stateTransition_InvalidTransition_ThrowsException() {
        Borrower borrower = new Borrower("B001", "Ismail", 600, new BigDecimal("10000000"));
        Loan loan = new Loan("L001", borrower, new BigDecimal("4000000"), 12, "Modal Usaha");
        
        assertThrows(IllegalStateException.class, loan::approve, "Cannot approve a draft loan");
    }

    @Test
    void stateTransition_DisburseFlow() {
        Borrower borrower = new Borrower("B001", "Ismail", 600, new BigDecimal("10000000"));
        Loan loan = new Loan("L001", borrower, new BigDecimal("4000000"), 12, "Modal Usaha");
        
        loan.submit();
        loan.review();
        loan.approve();
        
        assertEquals(LoanStatus.FUNDING, loan.getStatus());
        
        loan.disburse();
        assertEquals(LoanStatus.DISBURSED, loan.getStatus());
    }
}