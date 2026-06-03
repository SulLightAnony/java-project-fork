package com.p2plending.domain.funding;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Funding Domain Tests")
class FundingTest {

    private static final BigDecimal TARGET = new BigDecimal("10000000");
    private Funding funding;

    @BeforeEach
    void setUp() {
        funding = new Funding("LOAN-001", TARGET);
    }

    // Skenario 1 & 2: Kontribusi & Kalkulasi

    @Test
    @DisplayName("Kontribusi valid berhasil dicatat")
    void shouldRecordValidContribution() {
        funding.addContribution("LENDER-A", new BigDecimal("3000000"));

        assertEquals(1, funding.getContributions().size());
        assertEquals(0, funding.getTotalFunded().compareTo(new BigDecimal("3000000")));
    }

    @Test
    @DisplayName("Total dana akurat dari beberapa lender")
    void shouldAccumulateFundsFromMultipleLenders() {
        funding.addContribution("LENDER-A", new BigDecimal("4000000"));
        funding.addContribution("LENDER-B", new BigDecimal("3000000"));

        assertEquals(0, funding.getTotalFunded().compareTo(new BigDecimal("7000000")));
    }

    @Test
    @DisplayName("Persentase progres dihitung dengan benar")
    void shouldCalculateProgressPercentageCorrectly() {
        funding.addContribution("LENDER-A", new BigDecimal("4000000"));
        funding.addContribution("LENDER-B", new BigDecimal("3000000"));

        assertEquals(0, funding.getProgressPercentage().compareTo(new BigDecimal("70")));
    }

    // Skenario 3: Fully Funded

    @Test
    @DisplayName("isFullyFunded() false jika belum 100%")
    void shouldNotBeFullyFundedWhenBelowTarget() {
        funding.addContribution("LENDER-A", new BigDecimal("9000000"));

        assertFalse(funding.isFullyFunded());
    }

    @Test
    @DisplayName("isFullyFunded() true saat total dana mencapai target")
    void shouldBeFullyFundedWhenTargetReached() {
        funding.addContribution("LENDER-A", new BigDecimal("9000000"));
        funding.addContribution("LENDER-C", new BigDecimal("1000000"));

        assertTrue(funding.isFullyFunded());
        assertEquals(0, funding.getProgressPercentage().compareTo(new BigDecimal("100")));
    }

    @Test
    @DisplayName("getContributingLenderIds() berisi semua lender yang masuk")
    void shouldReturnAllContributingLenderIds() {
        funding.addContribution("LENDER-A", new BigDecimal("5000000"));
        funding.addContribution("LENDER-B", new BigDecimal("5000000"));

        Set<String> ids = funding.getContributingLenderIds();
        assertEquals(2, ids.size());
        assertTrue(ids.contains("LENDER-A"));
        assertTrue(ids.contains("LENDER-B"));
    }

    // Skenario 4 & 6: Validasi Input

    @Test
    @DisplayName("Kontribusi ditolak jika melebihi sisa target")
    void shouldRejectContributionExceedingRemainingTarget() {
        funding.addContribution("LENDER-A", new BigDecimal("9500000"));

        BigDecimal excessAmount = new BigDecimal("2000000");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
            funding.addContribution("LENDER-D", excessAmount)
        );
        assertTrue(ex.getMessage().contains("melebihi sisa dana"));
    }

    @Test
    @DisplayName("Kontribusi Rp 0 ditolak")
    void shouldRejectZeroContribution() {
        BigDecimal zeroAmount = BigDecimal.ZERO;
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
            funding.addContribution("LENDER-A", zeroAmount)
        );
        assertTrue(ex.getMessage().contains("lebih dari nol"));
    }

    @Test
    @DisplayName("Kontribusi negatif ditolak")
    void shouldRejectNegativeContribution() {
        BigDecimal negativeAmount = new BigDecimal("-500000");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
            funding.addContribution("LENDER-A", negativeAmount)
        );
        assertTrue(ex.getMessage().contains("lebih dari nol"));
    }

    @Test
    @DisplayName("Dana tidak berubah setelah kontribusi ditolak")
    void shouldNotChangeTotalWhenContributionRejected() {
        funding.addContribution("LENDER-A", new BigDecimal("9500000"));

        BigDecimal excessAmount = new BigDecimal("2000000");
        assertThrows(IllegalArgumentException.class, () ->
            funding.addContribution("LENDER-D", excessAmount)
        );
        assertEquals(0, funding.getTotalFunded().compareTo(new BigDecimal("9500000")));
    }

    // Skenario 7: Lender Berkontribusi Lebih dari Sekali

    @Test
    @DisplayName("Lender yang sama bisa berkontribusi beberapa kali")
    void shouldAllowSameLenderToContributeMultipleTimes() {
        funding.addContribution("LENDER-A", new BigDecimal("2000000"));
        funding.addContribution("LENDER-A", new BigDecimal("1000000"));

        assertEquals(2, funding.getContributions().size());
        assertEquals(0, funding.getTotalFunded().compareTo(new BigDecimal("3000000")));
    }

    @Test
    @DisplayName("Total kontribusi per lender dihitung akurat")
    void shouldCalculateTotalContributionPerLender() {
        funding.addContribution("LENDER-A", new BigDecimal("2000000"));
        funding.addContribution("LENDER-A", new BigDecimal("1000000"));
        funding.addContribution("LENDER-B", new BigDecimal("4000000"));

        assertEquals(0, funding.getTotalContributionBy("LENDER-A")
            .compareTo(new BigDecimal("3000000")));
    }
}