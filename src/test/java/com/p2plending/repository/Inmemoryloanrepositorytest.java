package com.p2plending.repository;

import com.p2plending.domain.loan.Borrower;
import com.p2plending.domain.loan.Loan;
import com.p2plending.repository.impl.InMemoryLoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("InMemoryLoanRepository")
class InMemoryLoanRepositoryTest {

    private InMemoryLoanRepository repository;
    private Loan loan;

    @BeforeEach
    void setUp() {
        repository = new InMemoryLoanRepository();
        Borrower borrower = new Borrower("B001", "Budi Santoso", 650, new BigDecimal("10000000"));
        loan = new Loan("L001", borrower, new BigDecimal("5000000"), 12, "Modal Usaha");
    }

    @Test
    @DisplayName("save() - berhasil menyimpan loan baru")
    void save_shouldStoreLoan() {
        repository.save(loan);

        Optional<Loan> result = repository.findById("L001");

        assertTrue(result.isPresent());
        assertEquals("L001", result.get().getId());
    }

    @Test
    @DisplayName("findById() - mengembalikan Optional berisi loan jika ID ditemukan")
    void findById_shouldReturnLoan_whenIdExists() {
        repository.save(loan);

        Optional<Loan> result = repository.findById("L001");

        assertTrue(result.isPresent());
        assertEquals(loan, result.get());
    }

    @Test
    @DisplayName("findById() - mengembalikan Optional kosong jika ID tidak ditemukan")
    void findById_shouldReturnEmpty_whenIdNotFound() {
        Optional<Loan> result = repository.findById("TIDAK_ADA");

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("save() - update menimpa data loan lama dengan ID yang sama")
    void save_shouldOverwriteExistingLoan_whenSameId() {
        repository.save(loan);

        // Simpan ulang loan yang sama (simulasi update status)
        repository.save(loan);

        Optional<Loan> result = repository.findById("L001");
        assertTrue(result.isPresent());
        assertEquals("L001", result.get().getId());
    }

    @Test
    @DisplayName("save() - menyimpan beberapa loan dengan ID berbeda")
    void save_shouldStoreMultipleLoans() {
        Borrower borrower2 = new Borrower("B002", "Siti Rahayu", 600, new BigDecimal("8000000"));
        Loan loan2 = new Loan("L002", borrower2, new BigDecimal("3000000"), 6, "Kebutuhan Konsumtif");

        repository.save(loan);
        repository.save(loan2);

        assertTrue(repository.findById("L001").isPresent());
        assertTrue(repository.findById("L002").isPresent());
    }

    @Test
    @DisplayName("findById() - repository kosong mengembalikan Optional kosong")
    void findById_shouldReturnEmpty_whenRepositoryIsEmpty() {
        Optional<Loan> result = repository.findById("L001");
        assertFalse(result.isPresent());
    }
}