package com.p2plending.stepdefs;

import com.p2plending.domain.loan.Borrower;
import com.p2plending.domain.loan.Loan;
import com.p2plending.domain.loan.LoanStatus;
import com.p2plending.repository.BorrowerRepository;
import com.p2plending.repository.LoanRepository;
import com.p2plending.repository.impl.InMemoryBorrowerRepository;
import com.p2plending.repository.impl.InMemoryLoanRepository;
import com.p2plending.service.LoanApplicationService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoanApplicationSteps {

    private final BorrowerRepository borrowerRepository = new InMemoryBorrowerRepository();
    private final LoanRepository loanRepository = new InMemoryLoanRepository();
    private final LoanApplicationService loanService = new LoanApplicationService(borrowerRepository, loanRepository);
    
    private String lastLoanId;
    private Exception lastException;
    private Borrower currentBorrower;

    @Given("Borrower dengan ID {string}, nama {string}, credit score {int} memiliki sisa limit {double}")
    public void borrower_dengan_id_nama_credit_score_memiliki_sisa_limit(String id, String name, int score, double limit) {
        currentBorrower = new Borrower(id, name, score, BigDecimal.valueOf(limit));
        borrowerRepository.save(currentBorrower);
    }

    @When("Borrower mengajukan pinjaman sebesar {double} dengan tenor {int} bulan untuk {string}")
    public void borrower_mengajukan_pinjaman_sebesar_dengan_tenor_bulan_untuk(double amount, int tenor, String purpose) {
        try {
            lastLoanId = loanService.apply(currentBorrower.getId(), BigDecimal.valueOf(amount), tenor, purpose);
        } catch (Exception e) {
            lastException = e;
        }
    }

    @Then("Sistem berhasil membuat pinjaman dengan status awal {string}")
    public void sistem_berhasil_membuat_pinjaman_dengan_status_awal(String expectedStatus) {
        assertNotNull(lastLoanId);
        Loan loan = loanRepository.findById(lastLoanId).orElse(null);
        assertNotNull(loan);
        assertEquals(LoanStatus.valueOf(expectedStatus), loan.getStatus());
    }

    @And("Limit borrower {string} berkurang menjadi {double}")
    public void limit_borrower_berkurang_menjadi(String borrowerId, double expectedLimit) {
        Borrower b = borrowerRepository.findById(borrowerId).orElse(null);
        assertNotNull(b);
        assertEquals(0, BigDecimal.valueOf(expectedLimit).compareTo(b.getRemainingLimit()));
    }

    @Then("Sistem menolak pengajuan dengan error {string}")
    public void sistem_menolak_pengajuan_dengan_error(String expectedError) {
        assertNotNull(lastException);
        assertEquals(expectedError, lastException.getMessage());
    }

    @Given("Pengajuan pinjaman {string} saat ini berada pada status {string}")
    public void pengajuan_pinjaman_saat_ini_berada_pada_status(String loanId, String status) {
        // Setup initial loan for transition test
        Borrower b = new Borrower("B999", "Test", 700, BigDecimal.valueOf(10000000));
        borrowerRepository.save(b);
        lastLoanId = loanService.apply(b.getId(), BigDecimal.valueOf(1000000), 12, "Test");
        // Status is DRAFT by default, if we need PENDING we'd call submit()
    }

    @When("Borrower melakukan submit pengajuan tersebut")
    public void borrower_melakukan_submit_pengajuan_tersebut() {
        Loan loan = loanRepository.findById(lastLoanId).orElseThrow();
        loan.submit();
    }

    @Then("Status pinjaman berubah menjadi {string}")
    public void status_pinjaman_berubah_menjadi(String expectedStatus) {
        Loan loan = loanRepository.findById(lastLoanId).orElseThrow();
        assertEquals(LoanStatus.valueOf(expectedStatus), loan.getStatus());
    }
}
