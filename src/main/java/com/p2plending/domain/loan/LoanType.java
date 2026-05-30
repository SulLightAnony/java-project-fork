package com.p2plending.domain.loan;

public enum LoanType {
    /**
    * Pinjaman untuk Usaha Mikro, Kecil, dan Menengah.
    * - Limit maksimum lebih besar
    * - Tenor bisa lebih panjang (hingga 36 bulan)
    * - Membutuhkan credit score minimal 500
    */
    UMKM,

    /**
    * Pinjaman untuk kebutuhan konsumtif / personal.
    * - Limit maksimum lebih kecil
    * - Tenor lebih pendek (maksimal 24 bulan)
    * - Membutuhkan credit score minimal 500
    */
    KONSUMTIF
}
