package com.p2plending.service;

import com.p2plending.domain.loan.Borrower;
import com.p2plending.domain.loan.Loan;
import com.p2plending.repository.BorrowerRepository;
import com.p2plending.repository.LoanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanApplicationServiceTest {

    @Mock
    private BorrowerRepository borrowerRepository;

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private LoanApplicationService service;

    @Test
    void apply_Success_ShouldSaveLoanAndBorrower() {
        // Arrange
        Borrower mockBorrower = new Borrower("B001", "Ismail", 600, new BigDecimal("10000000"));
        when(borrowerRepository.findById("B001")).thenReturn(Optional.of(mockBorrower));

        // Act
        String loanId = service.apply("B001", new BigDecimal("4000000"), 12, "Modal Usaha");

        // Assert
        assertNotNull(loanId);
        verify(loanRepository, times(1)).save(any(Loan.class));
        verify(borrowerRepository, times(1)).save(mockBorrower);
    }

    @Test
    void apply_BorrowerNotEligible_ShouldThrowException() {
        // Arrange
        Borrower lowScoreBorrower = new Borrower("B002", "LowScore", 400, new BigDecimal("5000000"));
        when(borrowerRepository.findById("B002")).thenReturn(Optional.of(lowScoreBorrower));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.apply("B002", new BigDecimal("1000000"), 12, "Kebutuhan");
        });
        assertEquals("Borrower is not eligible for a loan", exception.getMessage());
        verify(loanRepository, never()).save(any());
    }

    @Test
    void apply_InsufficientLimit_ShouldThrowException() {
        // Arrange
        Borrower mockBorrower = new Borrower("B001", "Ismail", 600, new BigDecimal("1000000"));
        when(borrowerRepository.findById("B001")).thenReturn(Optional.of(mockBorrower));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.apply("B001", new BigDecimal("2000000"), 12, "Modal");
        });
        assertEquals("Insufficient borrowing limit", exception.getMessage());
    }
}