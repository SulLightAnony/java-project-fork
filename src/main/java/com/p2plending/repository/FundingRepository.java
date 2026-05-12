package com.p2plending.repository;

import com.p2plending.domain.funding.Funding;
import java.util.Optional;

public interface FundingRepository {
    Optional<Funding> findByLoanId(String loanId);
    void save(Funding funding);
}
