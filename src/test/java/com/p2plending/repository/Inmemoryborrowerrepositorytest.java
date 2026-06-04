package com.p2plending.repository;

import com.p2plending.domain.loan.Borrower;
import com.p2plending.repository.impl.InMemoryBorrowerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("InMemoryBorrowerRepository")
class InMemoryBorrowerRepositoryTest {

    private InMemoryBorrowerRepository repository;
    private Borrower borrower;

    @BeforeEach
    void setUp() {
        repository = new InMemoryBorrowerRepository();
        borrower = new Borrower("B001", "Budi Santoso", 650, new BigDecimal("10000000"));
    }

    @Test
    @DisplayName("save() - berhasil menyimpan borrower baru")
    void save_shouldStoreBorrower() {
        repository.save(borrower);

        Optional<Borrower> result = repository.findById("B001");

        assertTrue(result.isPresent());
        assertEquals("B001", result.get().getId());
        assertEquals("Budi Santoso", result.get().getName());
    }

    @Test
    @DisplayName("findById() - mengembalikan Optional berisi borrower jika ID ditemukan")
    void findById_shouldReturnBorrower_whenIdExists() {
        repository.save(borrower);

        Optional<Borrower> result = repository.findById("B001");

        assertTrue(result.isPresent());
        assertEquals(borrower, result.get());
    }

    @Test
    @DisplayName("findById() - mengembalikan Optional kosong jika ID tidak ditemukan")
    void findById_shouldReturnEmpty_whenIdNotFound() {
        Optional<Borrower> result = repository.findById("TIDAK_ADA");

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("save() - update menimpa data lama dengan ID yang sama")
    void save_shouldOverwriteExistingBorrower_whenSameId() {
        repository.save(borrower);

        Borrower updated = new Borrower("B001", "Budi Update", 700, new BigDecimal("20000000"));
        repository.save(updated);

        Optional<Borrower> result = repository.findById("B001");

        assertTrue(result.isPresent());
        assertEquals("Budi Update", result.get().getName());
        assertEquals(700, result.get().getCreditScore());
    }

    @Test
    @DisplayName("save() - menyimpan beberapa borrower dengan ID berbeda")
    void save_shouldStoreMultipleBorrowers() {
        Borrower borrower2 = new Borrower("B002", "Siti Rahayu", 600, new BigDecimal("5000000"));

        repository.save(borrower);
        repository.save(borrower2);

        assertTrue(repository.findById("B001").isPresent());
        assertTrue(repository.findById("B002").isPresent());
    }
}