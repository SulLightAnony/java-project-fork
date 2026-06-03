package com.p2plending.domain.loan;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.function.Executable;

class LoanFactoryTest {
    private Borrower createBorrower(BigDecimal limit){
        return new Borrower("B001","Test Borrower",600,limit);
    }

    @Test
    void createLoan_UMKM_ValidParams_ShouldReturnLoan(){
        Borrower borrower = createBorrower(new BigDecimal("500000000"));

        Loan loan = LoanFactory.createLoan(LoanType.UMKM, borrower,new BigDecimal("200000000") , 36, "Ekspansi toko", "LOAN-001");

        assertNotNull(loan);
        assertEquals("LOAN-001", loan.getId());
        assertEquals(LoanStatus.DRAFT, loan.getStatus());
    }

    @Test
    void createLoan_UMKM_ExceedMaxAmount_ShouldThrowException() {
        Borrower borrower = createBorrower(new BigDecimal("999999999"));
        BigDecimal loanAmount = new BigDecimal("500000001");

        Executable executable = () ->
            LoanFactory.createLoan(LoanType.UMKM, borrower, loanAmount, 24, "Modal usaha", "LOAN-X");

        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    void createLoan_KONSUMTIF_ExceedMaxTenor_ShouldThrowException() {
        Borrower borrower = createBorrower(new BigDecimal("999999999"));

        Executable executable = () ->
            LoanFactory.createLoan(LoanType.KONSUMTIF, borrower, new BigDecimal("500000"), 25, "Modal usaha", "LOAN-X");

        assertThrows(IllegalArgumentException.class, executable);
    }
}
