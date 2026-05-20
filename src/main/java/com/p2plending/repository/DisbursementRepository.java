package com.p2plending.repository;

import com.p2plending.domain.disbursement.Disbursement;

import java.util.Optional;

public interface DisbursementRepository {
    void save(Disbursement disbursement);
    Optional<Disbursement> findByLoanId(String loanId);
}
