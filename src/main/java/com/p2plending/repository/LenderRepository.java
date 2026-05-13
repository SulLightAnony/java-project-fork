package com.p2plending.repository;

import com.p2plending.domain.funding.Lender;
import java.util.Optional;
import java.util.List;

public interface LenderRepository {
    void save(Lender lender);
    Optional<Lender> findById(String id);
    List<Lender> findAll();
}