package com.p2plending.repository;

import com.p2plending.domain.disbursement.Disbursement;

import java.util.Optional;

public interface DisbursementRepository {
    void save(Disbursement disbursement);
    Optional<Disbursement> findByLoanId(String loanId);
}
import java.util.List;
import java.util.Optional;

/**
 * Kontrak antarmuka untuk penyimpanan data Disbursement.
 */
public interface DisbursementRepository {
    
    /**
     * Menyimpan data pencairan baru atau memperbarui yang sudah ada.
     */
    void save(Disbursement disbursement);

    /**
     * Mencari data pencairan berdasarkan ID.
     */
    Optional<Disbursement> findById(String id);

    /**
     * Mengambil semua data pencairan yang pernah dilakukan.
     */
    List<Disbursement> findAll();
}
