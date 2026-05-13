package com.p2plending.notification;

import com.p2plending.domain.funding.Funding;
import com.p2plending.domain.funding.FundingObserver;
import com.p2plending.repository.FundingRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Publisher untuk event yang terjadi dalam proses funding.
 * FundingService memanggil publisher ini; publisher fetch objek Funding
 * dari repository lalu meneruskan event ke semua observer yang terdaftar.
 */
public class FundingEventPublisher {

    private final FundingRepository fundingRepository;
    private final List<FundingObserver> observers = new ArrayList<>();

    public FundingEventPublisher(FundingRepository fundingRepository) {
        this.fundingRepository = fundingRepository;
    }

    public void registerObserver(FundingObserver observer) {
        observers.add(observer);
    }

    /**
     * Dipanggil saat sebuah pinjaman telah fully funded.
     *
     * @param loanId ID pinjaman yang telah mencapai target dana
     */
    public void publishFundingCompleted(String loanId) {
        Funding funding = fundingRepository.findByLoanId(loanId)
                .orElseThrow(() -> new IllegalStateException(
                        "Funding tidak ditemukan saat publish event: " + loanId));

        for (FundingObserver observer : observers) {
            observer.onFullyFunded(funding);
        }
    }
}