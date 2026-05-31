package com.p2plending.domain.cashflow;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * TDD untuk Composite Pattern — Orang 3
 * Menguji SingleCashflow (Leaf) dan LoanCashflow (Composite)
 */
@DisplayName("Cashflow Composite Pattern Tests")
class CashflowTest {

    // ─── Skenario 1: SingleCashflow (Leaf) ───────────────────────────────────

    @Test
    @DisplayName("SingleCashflow mengembalikan amount dan type yang benar")
    void shouldReturnCorrectAmountAndTypeForSingleCashflow() {
        Cashflow principal = new SingleCashflow(CashflowType.PRINCIPAL, new BigDecimal("1000000"));

        assertEquals(0, principal.getAmount().compareTo(new BigDecimal("1000000")));
        assertEquals(CashflowType.PRINCIPAL, principal.getType());
    }

    @Test
    @DisplayName("SingleCashflow dengan tipe INTEREST mengembalikan type INTEREST")
    void shouldReturnInterestTypeForInterestCashflow() {
        Cashflow interest = new SingleCashflow(CashflowType.INTEREST, new BigDecimal("150000"));

        assertEquals(CashflowType.INTEREST, interest.getType());
        assertEquals(0, interest.getAmount().compareTo(new BigDecimal("150000")));
    }

    @Test
    @DisplayName("SingleCashflow menolak amount negatif")
    void shouldRejectNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () ->
            new SingleCashflow(CashflowType.PRINCIPAL, new BigDecimal("-1000"))
        );
    }

    // ─── Skenario 2: LoanCashflow (Composite) — Agregasi ─────────────────────

    @Test
    @DisplayName("LoanCashflow menggabungkan amount dari semua child")
    void shouldAggregateAmountFromAllChildren() {
        LoanCashflow loanCashflow = new LoanCashflow("Total Cashflow Loan #001");

        loanCashflow.add(new SingleCashflow(CashflowType.PRINCIPAL, new BigDecimal("1000000")));
        loanCashflow.add(new SingleCashflow(CashflowType.INTEREST,  new BigDecimal("150000")));

        assertEquals(0, loanCashflow.getAmount().compareTo(new BigDecimal("1150000")));
    }

    @Test
    @DisplayName("LoanCashflow kosong mengembalikan amount nol")
    void shouldReturnZeroWhenNoChildren() {
        LoanCashflow loanCashflow = new LoanCashflow("Cashflow Kosong");

        assertEquals(0, loanCashflow.getAmount().compareTo(BigDecimal.ZERO));
    }

    @Test
    @DisplayName("LoanCashflow dengan tiga komponen menjumlahkan semua dengan benar")
    void shouldSumThreeComponentsCorrectly() {
        LoanCashflow loanCashflow = new LoanCashflow("Total Cashflow Loan #002");

        loanCashflow.add(new SingleCashflow(CashflowType.PRINCIPAL, new BigDecimal("5000000")));
        loanCashflow.add(new SingleCashflow(CashflowType.INTEREST,  new BigDecimal("600000")));
        loanCashflow.add(new SingleCashflow(CashflowType.INTEREST,  new BigDecimal("400000")));

        assertEquals(0, loanCashflow.getAmount().compareTo(new BigDecimal("6000000")));
    }

    // ─── Skenario 3: Composite bersarang (Composite di dalam Composite) ───────

    @Test
    @DisplayName("LoanCashflow dapat berisi LoanCashflow lain sebagai child")
    void shouldSupportNestedComposite() {
        LoanCashflow cicilan1 = new LoanCashflow("Cicilan Bulan 1");
        cicilan1.add(new SingleCashflow(CashflowType.PRINCIPAL, new BigDecimal("500000")));
        cicilan1.add(new SingleCashflow(CashflowType.INTEREST,  new BigDecimal("75000")));

        LoanCashflow cicilan2 = new LoanCashflow("Cicilan Bulan 2");
        cicilan2.add(new SingleCashflow(CashflowType.PRINCIPAL, new BigDecimal("500000")));
        cicilan2.add(new SingleCashflow(CashflowType.INTEREST,  new BigDecimal("70000")));

        LoanCashflow totalCashflow = new LoanCashflow("Total Semua Cicilan");
        totalCashflow.add(cicilan1);
        totalCashflow.add(cicilan2);

        assertEquals(0, totalCashflow.getAmount().compareTo(new BigDecimal("1145000")));
    }

    // ─── Skenario 4: Manajemen Child ─────────────────────────────────────────

    @Test
    @DisplayName("LoanCashflow mencatat jumlah child yang benar")
    void shouldTrackNumberOfChildren() {
        LoanCashflow loanCashflow = new LoanCashflow("Cashflow Loan #003");

        loanCashflow.add(new SingleCashflow(CashflowType.PRINCIPAL, new BigDecimal("1000000")));
        loanCashflow.add(new SingleCashflow(CashflowType.INTEREST,  new BigDecimal("100000")));

        assertEquals(2, loanCashflow.getChildren().size());
    }

    @Test
    @DisplayName("LoanCashflow total berkurang setelah child dihapus")
    void shouldReduceAmountAfterRemovingChild() {
        LoanCashflow loanCashflow = new LoanCashflow("Cashflow Loan #004");

        SingleCashflow principal = new SingleCashflow(CashflowType.PRINCIPAL, new BigDecimal("1000000"));
        SingleCashflow interest  = new SingleCashflow(CashflowType.INTEREST,  new BigDecimal("100000"));

        loanCashflow.add(principal);
        loanCashflow.add(interest);
        loanCashflow.remove(interest);

        assertEquals(0, loanCashflow.getAmount().compareTo(new BigDecimal("1000000")));
        assertEquals(1, loanCashflow.getChildren().size());
    }
}