package com.p2plending.notification;

public interface FundingEventPublisher {
    /**
     * Dipanggil ketika sebuah pinjaman telah berhasil terpenuhi sepenuhnya oleh para lender.
     *
     * @param loanId ID pinjaman yang sudah fully funded
     */
    void publishFundingCompleted(String loanId);
}
