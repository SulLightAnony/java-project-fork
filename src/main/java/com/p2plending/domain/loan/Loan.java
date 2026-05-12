package com.p2plending.domain.loan;

import com.p2plending.domain.loan.state.DraftState;
import com.p2plending.domain.loan.state.LoanState;
import java.math.BigDecimal;

public class Loan {
    private final String id;
    private final Borrower borrower;
    private final BigDecimal amount;
    
    private LoanStatus status; // Untuk keperluan penyimpanan (Enum)
    private transient LoanState stateBehavior; // Untuk kontrol logika (Pattern)

    public Loan(String id, Borrower borrower, BigDecimal amount) {
        // Validasi Domain: Mencegah pembuatan objek jika limit tidak cukup
        if (!borrower.canBorrow(amount)) {
            throw new IllegalArgumentException("Limit pinjaman tidak mencukupi");
        }
        
        this.id = id;
        this.borrower = borrower;
        this.amount = amount;
        
        // Inisialisasi hybrid state
        this.status = LoanStatus.DRAFT;
        this.stateBehavior = new DraftState();
        
        // Kurangi limit borrower secara mutlak
        this.borrower.decreaseLimit(amount);
    }

    public String getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LoanStatus getStatus() {
        return status;
    }

    // Delegasi ke State Pattern
    public void submit() {
        this.stateBehavior.submit(this);
    }

    // Method ini hanya boleh dipanggil oleh class di dalam satu package (seperti DraftState)
    public void updateState(LoanState newState, LoanStatus newStatus) {
        this.stateBehavior = newState;
        this.status = newStatus;
    }
}