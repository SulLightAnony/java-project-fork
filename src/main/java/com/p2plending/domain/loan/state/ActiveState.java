package com.p2plending.domain.loan.state;

import com.p2plending.domain.loan.Loan;

public class ActiveState implements LoanState {

    @Override
    public void submit(Loan context) {
        throw new IllegalStateException("Gagal: Pinjaman sudah aktif.");
    }

    @Override
    public void review(Loan context) {
        throw new IllegalStateException("Gagal: Pinjaman sudah aktif.");
    }

    @Override
    public void approve(Loan context) {
        throw new IllegalStateException("Gagal: Pinjaman sudah aktif.");
    }

    @Override
    public void reject(Loan context) {
        throw new IllegalStateException("Gagal: Pinjaman sudah aktif.");
    }

    // Tidak memakai @Override karena Orang 1 tidak memasukkannya ke interface LoanState
    public void fund(Loan context) {
        throw new IllegalStateException("Gagal: Pinjaman sudah aktif, proses pendanaan telah ditutup.");
    }

    // Tidak memakai @Override karena Orang 1 tidak memasukkannya ke interface LoanState
    public void disburse(Loan context) {
        throw new IllegalStateException("Gagal: Pinjaman ini sudah dicairkan sebelumnya.");
    }
}