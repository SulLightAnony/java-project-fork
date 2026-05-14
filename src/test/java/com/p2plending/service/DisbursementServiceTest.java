package com.p2plending.service;

import com.p2plending.domain.disbursement.Disbursement;
import com.p2plending.domain.loan.Loan;
import com.p2plending.interest.InterestStrategy;
import com.p2plending.repository.DisbursementRepository;
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
public class DisbursementServiceTest {

    // Mock dependensi milik Orang 1
    @Mock
    private LoanRepository loanRepository;

    // Mock dependensi milik Orang 4
    @Mock
    private DisbursementRepository disbursementRepository;

    @Mock
    private InterestStrategy interestStrategy;

    // Service yang akan kita tes
    @InjectMocks
    private DisbursementService disbursementService;

    @Test
    void shouldDisburseLoanSuccessfully() {
        // =========================
        // ARRANGE
        // =========================
        String loanId = "L-001";
        Loan mockLoan = mock(Loan.class);
        
        // Asumsi data yang dikembalikan oleh Loan milik Orang 1
        when(mockLoan.getId()).thenReturn(loanId);
        when(mockLoan.getAmount()).thenReturn(new BigDecimal("10000000")); // Pokok pinjaman 10 juta
        when(mockLoan.getTenor()).thenReturn(12); // Tenor 12 bulan

        // Menyelinapkan mock object saat service mencari pinjaman
        when(loanRepository.findById(loanId)).thenReturn(Optional.of(mockLoan));
        
        // Asumsi hasil kalkulasi bunga dari Strategy Pattern adalah 1 Juta
        when(interestStrategy.calculate(any(), anyInt())).thenReturn(new BigDecimal("1000000"));

        // =========================
        // ACT
        // =========================
        Disbursement result = disbursementService.disburseLoan(loanId, interestStrategy);

        // =========================
        // ASSERT
        // =========================
        assertNotNull(result);
        assertEquals(loanId, result.getLoanId());
        
        // Memastikan state pattern berjalan (metode disburse di dalam Loan dipanggil)
        verify(mockLoan).disburse();
        
        // Memastikan data Loan yang baru dan record Disbursement disimpan ke repository
        verify(loanRepository).save(mockLoan);
        verify(disbursementRepository).save(any(Disbursement.class));
    }

    @Test
    void shouldThrowExceptionWhenLoanNotFound() {
        // Jika loan tidak ada di repository
        when(loanRepository.findById("L-999")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            disbursementService.disburseLoan("L-999", interestStrategy);
        });

        assertEquals("Pinjaman dengan ID L-999 tidak ditemukan.", exception.getMessage());
    }
}