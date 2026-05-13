package com.p2plending.service;

import com.p2plending.domain.funding.Funding;
import com.p2plending.domain.loan.Borrower;
import com.p2plending.domain.loan.Loan;
import com.p2plending.notification.FundingEventPublisher;
import com.p2plending.repository.BorrowerRepository;
import com.p2plending.repository.FundingRepository;
import com.p2plending.repository.LoanRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FundingService Tests")
class FundingServiceTest {

    // ─── Stub: LoanRepository ────────────────────────────────────────────────
    private static class InMemoryLoanRepository implements LoanRepository {
        private final Map<String, Loan> store = new HashMap<>();

        @Override
        public Optional<Loan> findById(String id) {
            return Optional.ofNullable(store.get(id));
        }

        @Override
        public void save(Loan loan) {
            store.put(loan.getId(), loan);
        }
    }

    // ─── Stub: FundingRepository ─────────────────────────────────────────────
    private static class InMemoryFundingRepository implements FundingRepository {
        private final Map<String, Funding> store = new HashMap<>();

        @Override
        public Optional<Funding> findByLoanId(String loanId) {
            return Optional.ofNullable(store.get(loanId));
        }

        @Override
        public void save(Funding funding) {
            store.put(funding.getLoanId(), funding);
        }
    }

    // ─── Stub: FundingEventPublisher ─────────────────────────────────────────
    private static class SpyEventPublisher implements FundingEventPublisher {
        private final List<String> publishedLoanIds = new ArrayList<>();

        @Override
        public void publishFundingCompleted(String loanId) {
            publishedLoanIds.add(loanId);
        }

        public boolean wasPublished(String loanId) {
            return publishedLoanIds.contains(loanId);
        }

        public int publishCount() {
            return publishedLoanIds.size();
        }
    }

    // ─── Setup ────────────────────────────────────────────────────────────────
    private static final String LOAN_ID    = "LOAN-001";
    private static final String LENDER_A   = "LENDER-A";
    private static final String LENDER_B   = "LENDER-B";
    private static final BigDecimal TARGET = new BigDecimal("10000000");

    private InMemoryLoanRepository    loanRepository;
    private InMemoryFundingRepository fundingRepository;
    private SpyEventPublisher         eventPublisher;
    private FundingService            fundingService;

    @BeforeEach
    void setUp() {
        loanRepository    = new InMemoryLoanRepository();
        fundingRepository = new InMemoryFundingRepository();
        eventPublisher    = new SpyEventPublisher();
        fundingService    = new FundingService(loanRepository, fundingRepository, eventPublisher);

        // Siapkan Loan valid di repository
       Borrower borrower = new Borrower("BORROWER-001", "John Doe", 70000000, TARGET);
        Loan loan = new Loan(LOAN_ID, borrower, TARGET, 12, "Modal Usaha");
        loanRepository.save(loan);
    }

    // ─── Skenario 1: Kontribusi Berhasil ─────────────────────────────────────

    @Test
    @DisplayName("Kontribusi valid berhasil disimpan ke FundingRepository")
    void shouldSaveFundingAfterValidContribution() {
        fundingService.contribute(LOAN_ID, LENDER_A, new BigDecimal("3000000"));

        Funding savedFunding = fundingRepository.findByLoanId(LOAN_ID).orElse(null);
        assertNotNull(savedFunding);
        assertEquals(0, savedFunding.getTotalFunded().compareTo(new BigDecimal("3000000")));
    }

    @Test
    @DisplayName("Beberapa lender dapat berkontribusi pada pinjaman yang sama")
    void shouldAccumulateContributionsFromMultipleLenders() {
        fundingService.contribute(LOAN_ID, LENDER_A, new BigDecimal("6000000"));
        fundingService.contribute(LOAN_ID, LENDER_B, new BigDecimal("2000000"));

        Funding savedFunding = fundingRepository.findByLoanId(LOAN_ID).orElseThrow();
        assertEquals(0, savedFunding.getTotalFunded().compareTo(new BigDecimal("8000000")));
        assertEquals(2, savedFunding.getContributions().size());
    }

    // ─── Skenario 2: Event Notifikasi ────────────────────────────────────────

    @Test
    @DisplayName("Event tidak dipublikasikan jika funding belum penuh")
    void shouldNotPublishEventWhenNotFullyFunded() {
        fundingService.contribute(LOAN_ID, LENDER_A, new BigDecimal("9000000"));

        assertFalse(eventPublisher.wasPublished(LOAN_ID));
    }

    @Test
    @DisplayName("Event dipublikasikan tepat sekali saat funding mencapai target")
    void shouldPublishEventWhenFullyFunded() {
        fundingService.contribute(LOAN_ID, LENDER_A, new BigDecimal("9000000"));
        fundingService.contribute(LOAN_ID, LENDER_B, new BigDecimal("1000000"));

        assertTrue(eventPublisher.wasPublished(LOAN_ID));
        assertEquals(1, eventPublisher.publishCount());
    }

    // ─── Skenario 3: Validasi Pinjaman ───────────────────────────────────────

    @Test
    @DisplayName("Exception dilempar jika loanId tidak ditemukan")
    void shouldThrowWhenLoanNotFound() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
            fundingService.contribute("LOAN-TIDAK-ADA", LENDER_A, new BigDecimal("1000000"))
        );
        assertTrue(ex.getMessage().contains("tidak ditemukan"));
    }

    // ─── Skenario 4: Validasi Kontribusi (didelegasikan ke domain) ───────────

    @Test
    @DisplayName("Exception dilempar jika kontribusi melebihi sisa target")
    void shouldThrowWhenContributionExceedsRemaining() {
        fundingService.contribute(LOAN_ID, LENDER_A, new BigDecimal("9500000"));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
            fundingService.contribute(LOAN_ID, LENDER_B, new BigDecimal("2000000"))
        );
        assertTrue(ex.getMessage().contains("melebihi sisa dana"));
    }

    @Test
    @DisplayName("Exception dilempar jika kontribusi bernilai nol")
    void shouldThrowWhenContributionIsZero() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
            fundingService.contribute(LOAN_ID, LENDER_A, BigDecimal.ZERO)
        );
        assertTrue(ex.getMessage().contains("lebih dari nol"));
    }

    @Test
    @DisplayName("Funding tidak berubah jika kontribusi ditolak")
    void shouldNotChangeFundingWhenContributionRejected() {
        fundingService.contribute(LOAN_ID, LENDER_A, new BigDecimal("9500000"));

        assertThrows(IllegalArgumentException.class, () ->
            fundingService.contribute(LOAN_ID, LENDER_B, new BigDecimal("2000000"))
        );

        Funding savedFunding = fundingRepository.findByLoanId(LOAN_ID).orElseThrow();
        assertEquals(0, savedFunding.getTotalFunded().compareTo(new BigDecimal("9500000")));
    }
}
