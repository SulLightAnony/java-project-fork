package com.p2plending.repository.impl;

import com.p2plending.domain.loan.Borrower;
import com.p2plending.repository.BorrowerRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryBorrowerRepository implements BorrowerRepository {
    private final Map<String, Borrower> storage = new HashMap<>();

    @Override
    public Optional<Borrower> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public void save(Borrower borrower) {
        storage.put(borrower.getId(), borrower);
    }
}
