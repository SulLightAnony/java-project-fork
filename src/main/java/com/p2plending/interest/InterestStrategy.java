package com.p2plending.interest;

import java.math.BigDecimal;

/**
 * Interface induk untuk GoF Strategy Pattern.
 * Digunakan untuk menghitung bunga/margin secara dinamis.
 */
public interface InterestStrategy {
    
    /**
     * Menghitung total bunga berdasarkan pokok pinjaman dan tenor.
     * * @param principal Pokok pinjaman (jumlah yang dicairkan)
     * @param tenorInMonths Lama pinjaman dalam bulan
     * @return Total bunga atau margin yang harus dibayar
     */
    BigDecimal calculate(BigDecimal principal, int tenorInMonths);
}
