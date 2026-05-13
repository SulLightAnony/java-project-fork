package com.p2plending.service;

import com.p2plending.domain.loan.Loan;
import com.p2plending.repository.LoanRepository;
import com.p2plending.interest.InterestStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DisbursementServiceTest {

    @Mock
    private LoanRepository loanRepository; // Dummy repository

    @Mock
    private InterestStrategy interestStrategy; // Dummy kalkulator bunga

    @InjectMocks
    private DisbursementService disbursementService;

    @Test
    void shouldDisburseLoanSuccessfully() {
        String loanId = "L-001";
        Loan mockLoan = mock(Loan.class);

        // Asumsi pencarian loan berhasil dan mengembalikan mockLoan
        when(loanRepository.findById(loanId)).thenReturn(Optional.of(mockLoan));
        
        // Asumsi hitungan bunga dari strategy mengembalikan 1 juta
        when(interestStrategy.calculate(any(), anyInt())).thenReturn(new BigDecimal("1000000"));

        disbursementService.disburseLoan(loanId);

        verify(mockLoan).disburse(); 
        verify(loanRepository).save(mockLoan);
    }
}