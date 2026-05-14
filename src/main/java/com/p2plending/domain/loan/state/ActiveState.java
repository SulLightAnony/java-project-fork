package com.p2plending.domain.loan.state;

import com.p2plending.domain.loan.Loan;
import com.p2plending.domain.loan.LoanStatus;
import java.math.BigDecimal;

public class ActiveState implements LoanState {

    @Override
    public void submit(Loan loan) {
        throw new IllegalStateException("Gagal: Pinjaman sudah aktif dan sedang berjalan.");
    }

    @Override
    public void approve(Loan loan) {
        throw new IllegalStateException("Gagal: Pinjaman sudah aktif dan sedang berjalan.");
    }

    @Override
    public void reject(Loan loan) {
        throw new IllegalStateException("Gagal: Pinjaman sudah aktif dan sedang berjalan.");
    }

    public void fund(Loan loan, BigDecimal amount) {
        throw new IllegalStateException("Gagal: Pinjaman sudah aktif, proses pendanaan telah ditutup.");
    }

    public void disburse(Loan loan) {
        throw new IllegalStateException("Gagal: Pinjaman ini sudah dicairkan sebelumnya.");
    }

    @Override
    public void review(Loan loan) {
        throw new IllegalStateException("Gagal: Pinjaman sudah aktif dan sedang berjalan.");
    }
}