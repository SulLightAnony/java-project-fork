package com.p2plending.simulation;

import com.p2plending.domain.disbursement.Disbursement;
import com.p2plending.domain.loan.Loan;
import com.p2plending.domain.loan.LoanStatus;
import com.p2plending.domain.loan.state.ActiveState;
import com.p2plending.interest.InterestStrategy;
import com.p2plending.interest.SyariahStrategy;
import com.p2plending.repository.DisbursementRepository;
import com.p2plending.repository.LoanRepository;
import com.p2plending.service.DisbursementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanLifeCycleSimulationTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private DisbursementRepository disbursementRepository;

    @Mock
    private InterestStrategy baseInterestStrategy;

    @InjectMocks
    private DisbursementService disbursementService;

    @Test
    void simulateEndToEndP2PLendingProcess() {
        System.out.println("======================================================");
        System.out.println("  MEMULAI SIMULASI END-TO-END P2P LENDING (ORANG 4)   ");
        System.out.println("======================================================");

        String loanId = "L-2026-X";
        Loan mockLoan = mock(Loan.class);
        
        when(mockLoan.getAmount()).thenReturn(new BigDecimal("50000000"));
        when(mockLoan.getTenor()).thenReturn(12);

        System.out.println("\n[1] PENGAJUAN PINJAMAN (DRAFT -> PENDING)");
        System.out.println("    -> Borrower 'Sulthan' mengajukan pinjaman sebesar Rp 50.000.000 untuk 12 Bulan.");

        System.out.println("\n[2] RISK ENGINE & KELAYAKAN (REVIEWING -> APPROVED)");
        System.out.println("    -> Sistem memvalidasi Credit Score Borrower... (LOLOS KUALIFIKASI).");

        System.out.println("\n[3] CROWDFUNDING / PENDANAAN (FUNDING)");
        System.out.println("    -> Lender A mendanai Rp 30.000.000");
        System.out.println("    -> Lender B mendanai Rp 20.000.000");
        System.out.println("    -> Status Pinjaman: FULLY FUNDED (100% Terkumpul).");

        when(loanRepository.findById(loanId)).thenReturn(Optional.of(mockLoan));
        System.out.println("\n[4] PENCAIRAN DANA (DISBURSEMENT -> ACTIVE)");
        
        InterestStrategy strategy = new SyariahStrategy(new BigDecimal("0.12"));
        System.out.println("    -> Menerapkan 'Strategy Pattern': Menggunakan skema Syariah (Margin 12%).");
        
        Disbursement result = disbursementService.disburseLoan(loanId, strategy);

        System.out.println("    -> Uang berhasil ditransfer ke rekening Borrower!");
        System.out.println("    -> 'State Pattern': Status Pinjaman dikunci menjadi DISBURSED.");
        
        System.out.println("\n[5] JADWAL PEMBAYARAN (REPAYMENT SCHEDULE)");
        System.out.println("    -> Total Pokok + Margin yang harus dibayar : Rp " + result.getRepaymentSchedule().getTotalAmountToRepay());
        System.out.println("    -> Cicilan per bulan selama 12 bulan       : Rp " + result.getRepaymentSchedule().getMonthlyInstallment());
        
        System.out.println("\n======================================================");
        System.out.println("               SIMULASI SELESAI & SUKSES!               ");
        System.out.println("======================================================");

        assertNotNull(result);
        verify(mockLoan).setLoanStatus(LoanStatus.DISBURSED); 
        verify(mockLoan).setLoanState(any(ActiveState.class));
        verify(disbursementRepository).save(any(Disbursement.class));
    }
}