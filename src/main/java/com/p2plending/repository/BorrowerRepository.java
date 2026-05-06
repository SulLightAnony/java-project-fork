package main.java.com.p2plending.repository;

import com.p2plending.domain.loan.Borrower;
import java.util.Optional;

public interface BorrowerRepository {
    Optional<Borrower> findById(String id);
    void save(Borrower borrower);
}