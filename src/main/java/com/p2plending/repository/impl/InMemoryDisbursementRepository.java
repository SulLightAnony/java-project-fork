package com.p2plending.repository.impl;

import com.p2plending.domain.disbursement.Disbursement;
import com.p2plending.repository.DisbursementRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryDisbursementRepository implements DisbursementRepository {
    
    private final Map<String, Disbursement> store = new ConcurrentHashMap<>();

    @Override
    public void save(Disbursement disbursement) {
        if (disbursement == null || disbursement.getId() == null) {
            throw new IllegalArgumentException("Disbursement atau ID tidak boleh null");
        }
        store.put(disbursement.getId(), disbursement);
    }

    @Override
    public Optional<Disbursement> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Disbursement> findAll() {
        return new ArrayList<>(store.values());
    }
}