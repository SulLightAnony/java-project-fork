package com.p2plending.repository;

import com.p2plending.domain.loan.Loan;
import java.util.Optional;

public interface LoanRepository {
    Optional<Loan> findById(String id);
    void save(Loan loan);
}