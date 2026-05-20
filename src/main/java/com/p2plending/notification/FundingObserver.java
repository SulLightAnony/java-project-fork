package com.p2plending.notification;
import com.p2plending.domain.funding.Funding;

/**
 * Observer interface untuk menerima notifikasi event pada proses funding.
 * Implementasi bertanggung jawab atas mekanisme pengiriman notifikasi masing-masing.
 */
public interface FundingObserver {

    /**
     * Dipanggil saat sebuah pinjaman telah fully funded.
     *
     * @param funding objek Funding yang telah mencapai target dana
     */
    void onFullyFunded(Funding funding);
}