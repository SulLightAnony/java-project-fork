package com.p2plending.service;

import com.p2plending.domain.disbursement.Disbursement;
import com.p2plending.domain.loan.Loan;
import com.p2plending.repository.DisbursementRepository;
import com.p2plending.repository.LoanRepository;
import com.p2plending.interest.InterestStrategy;
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
public class DisbursementServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private DisbursementRepository disbursementRepository;

    @Mock
    private InterestStrategy interestStrategy;

    @InjectMocks
    private DisbursementService disbursementService;

    @Test
    void shouldDisburseLoanSuccessfully() {
        String loanId = "L-001";
        Loan mockLoan = mock(Loan.class);

        // Asumsi pencarian loan berhasil dan mengembalikan mockLoan
        when(loanRepository.findById(loanId)).thenReturn(Optional.of(mockLoan));
        when(mockLoan.getAmount()).thenReturn(new BigDecimal("10000000"));
        when(mockLoan.getTenor()).thenReturn(12);

        // Asumsi hitungan bunga dari strategy mengembalikan 1 juta
        
        
        when(mockLoan.getAmount()).thenReturn(new BigDecimal("10000000"));
        when(mockLoan.getTenor()).thenReturn(12);

        when(loanRepository.findById(loanId)).thenReturn(Optional.of(mockLoan));
        when(interestStrategy.calculate(any(), anyInt())).thenReturn(new BigDecimal("1000000"));

        Disbursement result = disbursementService.disburseLoan(loanId, interestStrategy);

        assertNotNull(result);
        assertEquals(loanId, result.getLoanId());
        
        verify(mockLoan).disburse();
        
        verify(loanRepository).save(mockLoan);
        verify(disbursementRepository).save(any(Disbursement.class));
    }

    @Test
    void shouldThrowExceptionWhenLoanNotFound() {
        when(loanRepository.findById("L-999")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            disbursementService.disburseLoan("L-999", interestStrategy);
        });

        assertEquals("Pinjaman dengan ID L-999 tidak ditemukan.", exception.getMessage());
    }
}