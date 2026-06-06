package com.p2plending.domain.loan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class LoanStateTest {

    private Borrower borrower;

    @BeforeEach
    void setUp() {
        borrower = new Borrower("B001", "Ismail", 600, new BigDecimal("999999999"));
    }

    // Helper untuk membuat Loan di state tertentu
    private Loan createLoanAtDraft() {
        return LoanFactory.createLoan(LoanType.UMKM, borrower, new BigDecimal("1000000"), 12, "Modal usaha", "L001");
    }

    private Loan createLoanAtPending() {
        Loan loan = createLoanAtDraft();
        loan.submit();
        return loan;
    }

    private Loan createLoanAtReviewing() {
        Loan loan = createLoanAtPending();
        loan.review();
        return loan;
    }

    private Loan createLoanAtFunding() {
        Loan loan = createLoanAtReviewing();
        loan.approve();
        return loan;
    }

    private Loan createLoanAtActive() {
        Loan loan = createLoanAtFunding();
        loan.disburse();
        return loan;
    }

    private Loan createLoanAtRejected() {
        Loan loan = createLoanAtReviewing();
        loan.reject();
        return loan;
    }

    // ================================================================
    // DRAFT STATE
    // ================================================================

    @Test
    void draftState_Submit_ShouldMoveToPending() {
        Loan loan = createLoanAtDraft();
        loan.submit();
        assertEquals(LoanStatus.PENDING, loan.getStatus());
    }

    @Test
    void draftState_Review_ShouldThrowException() {
        Loan loan = createLoanAtDraft();
        assertThrows(IllegalStateException.class, loan::review);
    }

    @Test
    void draftState_Approve_ShouldThrowException() {
        Loan loan = createLoanAtDraft();
        assertThrows(IllegalStateException.class, loan::approve);
    }

    @Test
    void draftState_Reject_ShouldThrowException() {
        Loan loan = createLoanAtDraft();
        assertThrows(IllegalStateException.class, loan::reject);
    }

    @Test
    void draftState_Disburse_ShouldThrowException() {
        Loan loan = createLoanAtDraft();
        assertThrows(IllegalStateException.class, loan::disburse);
    }

    // ================================================================
    // PENDING STATE
    // ================================================================

    @Test
    void pendingState_Review_ShouldMoveToReviewing() {
        Loan loan = createLoanAtPending();
        loan.review();
        assertEquals(LoanStatus.REVIEWING, loan.getStatus());
    }

    @Test
    void pendingState_Submit_ShouldThrowException() {
        Loan loan = createLoanAtPending();
        assertThrows(IllegalStateException.class, loan::submit);
    }

    @Test
    void pendingState_Approve_ShouldThrowException() {
        Loan loan = createLoanAtPending();
        assertThrows(IllegalStateException.class, loan::approve);
    }

    @Test
    void pendingState_Reject_ShouldThrowException() {
        Loan loan = createLoanAtPending();
        assertThrows(IllegalStateException.class, loan::reject);
    }

    @Test
    void pendingState_Disburse_ShouldThrowException() {
        Loan loan = createLoanAtPending();
        assertThrows(IllegalStateException.class, loan::disburse);
    }

    // ================================================================
    // REVIEWING STATE
    // ================================================================

    @Test
    void reviewingState_Approve_ShouldMoveToFunding() {
        Loan loan = createLoanAtReviewing();
        loan.approve();
        assertEquals(LoanStatus.FUNDING, loan.getStatus());
    }

    @Test
    void reviewingState_Reject_ShouldMoveToRejected() {
        Loan loan = createLoanAtReviewing();
        loan.reject();
        assertEquals(LoanStatus.REJECTED, loan.getStatus());
    }

    @Test
    void reviewingState_Submit_ShouldThrowException() {
        Loan loan = createLoanAtReviewing();
        assertThrows(IllegalStateException.class, loan::submit);
    }

    @Test
    void reviewingState_Review_ShouldThrowException() {
        Loan loan = createLoanAtReviewing();
        assertThrows(IllegalStateException.class, loan::review);
    }

    @Test
    void reviewingState_Disburse_ShouldThrowException() {
        Loan loan = createLoanAtReviewing();
        assertThrows(IllegalStateException.class, loan::disburse);
    }

    // ================================================================
    // FUNDING STATE
    // ================================================================

    @Test
    void fundingState_Disburse_ShouldMoveToActive() {
        Loan loan = createLoanAtFunding();
        loan.disburse();
        assertEquals(LoanStatus.DISBURSED, loan.getStatus());
    }

    @Test
    void fundingState_Submit_ShouldThrowException() {
        Loan loan = createLoanAtFunding();
        assertThrows(IllegalStateException.class, loan::submit);
    }

    @Test
    void fundingState_Review_ShouldThrowException() {
        Loan loan = createLoanAtFunding();
        assertThrows(IllegalStateException.class, loan::review);
    }

    @Test
    void fundingState_Approve_ShouldThrowException() {
        Loan loan = createLoanAtFunding();
        assertThrows(IllegalStateException.class, loan::approve);
    }

    @Test
    void fundingState_Reject_ShouldThrowException() {
        Loan loan = createLoanAtFunding();
        assertThrows(IllegalStateException.class, loan::reject);
    }

    // ================================================================
    // ACTIVE STATE
    // ================================================================

    @Test
    void activeState_Submit_ShouldThrowException() {
        Loan loan = createLoanAtActive();
        assertThrows(IllegalStateException.class, loan::submit);
    }

    @Test
    void activeState_Review_ShouldThrowException() {
        Loan loan = createLoanAtActive();
        assertThrows(IllegalStateException.class, loan::review);
    }

    @Test
    void activeState_Approve_ShouldThrowException() {
        Loan loan = createLoanAtActive();
        assertThrows(IllegalStateException.class, loan::approve);
    }

    @Test
    void activeState_Reject_ShouldThrowException() {
        Loan loan = createLoanAtActive();
        assertThrows(IllegalStateException.class, loan::reject);
    }

    @Test
    void activeState_Disburse_ShouldThrowException() {
        Loan loan = createLoanAtActive();
        assertThrows(IllegalStateException.class, loan::disburse);
    }

    // ================================================================
    // REJECTED STATE
    // ================================================================

    @Test
    void rejectedState_Submit_ShouldThrowException() {
        Loan loan = createLoanAtRejected();
        assertThrows(IllegalStateException.class, loan::submit);
    }

    @Test
    void rejectedState_Review_ShouldThrowException() {
        Loan loan = createLoanAtRejected();
        assertThrows(IllegalStateException.class, loan::review);
    }

    @Test
    void rejectedState_Approve_ShouldThrowException() {
        Loan loan = createLoanAtRejected();
        assertThrows(IllegalStateException.class, loan::approve);
    }

    @Test
    void rejectedState_Reject_ShouldThrowException() {
        Loan loan = createLoanAtRejected();
        assertThrows(IllegalStateException.class, loan::reject);
    }

    @Test
    void rejectedState_Disburse_ShouldThrowException() {
        Loan loan = createLoanAtRejected();
        assertThrows(IllegalStateException.class, loan::disburse);
    }

    // ================================================================
    // FULL FLOW
    // ================================================================

    @Test
    void fullFlow_DraftToPendingToReviewingToFundingToActive() {
        Loan loan = createLoanAtDraft();
        assertEquals(LoanStatus.DRAFT, loan.getStatus());

        loan.submit();
        assertEquals(LoanStatus.PENDING, loan.getStatus());

        loan.review();
        assertEquals(LoanStatus.REVIEWING, loan.getStatus());

        loan.approve();
        assertEquals(LoanStatus.FUNDING, loan.getStatus());

        loan.disburse();
        assertEquals(LoanStatus.DISBURSED, loan.getStatus());
    }

    @Test
    void fullFlow_DraftToPendingToReviewingToRejected() {
        Loan loan = createLoanAtDraft();

        loan.submit();
        loan.review();
        loan.reject();

        assertEquals(LoanStatus.REJECTED, loan.getStatus());
    }
}