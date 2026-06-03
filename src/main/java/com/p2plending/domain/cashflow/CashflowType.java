package com.p2plending.domain.cashflow;

/**
 * Tipe komponen cashflow dalam struktur pinjaman.
 * Composite Pattern 
 */
public enum CashflowType {
    PRINCIPAL,  // Pokok pinjaman
    INTEREST    // Bunga / margin
}