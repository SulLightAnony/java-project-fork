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
    void applyForLoan_ShouldSaveLoanToRepository() {
        // Arrange
        Borrower mockBorrower = new Borrower("B001", new BigDecimal("10000000"));
        when(borrowerRepository.findById("B001")).thenReturn(Optional.of(mockBorrower));

        // Act
        service.applyForLoan("B001", new BigDecimal("4000000"));

        // Assert
        verify(loanRepository, times(1)).save(any(Loan.class));
    }
}