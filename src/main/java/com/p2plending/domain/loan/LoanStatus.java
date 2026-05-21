package com.p2plending.domain.loan;

public enum LoanStatus {
    DRAFT,      // Pinjaman baru dibuat
    PENDING,    // Menunggu evaluasi
    REVIEWING,  // Sedang dalam proses review
    FUNDING,    // Sedang dalam proses pendanaan
    FUNDED,     // Dana sudah terpenuhi
    ACTIVE,     // Pinjaman aktif setelah pencairan
    DISBURSED,  // Dana sudah dicairkan
    REPAYMENT,  // Dalam proses pembayaran kembali
    COMPLETED,  // Pinjaman lunas
    REJECTED,   // Pinjaman ditolak
    CANCELLED,  // Pinjaman dibatalkan
    DEFAULTED   // Gagal bayar
}