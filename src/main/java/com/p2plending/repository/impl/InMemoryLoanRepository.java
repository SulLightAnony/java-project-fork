package com.p2plending.repository.impl;

import com.p2plending.domain.loan.Loan;
import com.p2plending.repository.LoanRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryLoanRepository implements LoanRepository {
    private final Map<String, Loan> storage = new HashMap<>();

    @Override
    public Optional<Loan> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public void save(Loan loan) {
        storage.put(loan.getId(), loan);
    }
}
