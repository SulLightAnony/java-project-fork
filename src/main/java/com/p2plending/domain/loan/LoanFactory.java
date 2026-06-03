package com.p2plending.domain.loan;

import java.math.BigDecimal;

public class LoanFactory {
    private static final BigDecimal MAX_AMOUNT_UMKM =  new BigDecimal("500000000");
    private static final BigDecimal MAX_AMOUNT_KONSUMTIF =  new BigDecimal("50000000");

    private static final int MAX_TENOR_UMKM = 36;
    private static final int MAX_TENOR_KONSUMTIF = 24;

    private LoanFactory(){}

    public static Loan createLoan(LoanType loanType,Borrower borrower,BigDecimal amount,int tenor,String purpose,String loanId){
        validateLoanType(loanType);
        validateAmount(loanType,amount);
        validateTenor(loanType,tenor);

        return new Loan(loanId,borrower,amount,tenor,purpose);
    }

    private static void validateLoanType(LoanType loanType){
        if(loanType == null){
            throw new IllegalArgumentException("LoanType tidak boleh null");
        }
    }

    private static void validateAmount(LoanType loanType,BigDecimal amount){
        if(amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Jumlah Pinjaman harus Lebih dari 0");
        }
        BigDecimal maxAmount = getMaxAmount(loanType);
        if(amount.compareTo(maxAmount) > 0){
            throw new IllegalArgumentException(
                String.format("Jumlah pinjaman untuk tipe %s tidak boleh melebihi %s. Diajukan %s",loanType, maxAmount,amount));
        }
    }

    private static void validateTenor(LoanType loanType, int tenor){
        if(tenor <= 0){
            throw new IllegalArgumentException("Tenor harus lebih daru 0 bulan");
        }
        int maxTenor = getMaxTenor(loanType);
        if(tenor > maxTenor){
            throw new IllegalArgumentException(
                String.format("Tenor untuk tipe %s tidak boleh melebihi %d bulan. Diajukan %d bulan ",loanType,maxTenor,tenor)
            );
        }
    }

    public static BigDecimal getMaxAmount(LoanType loanType){
        return switch (loanType){
            case UMKM  -> MAX_AMOUNT_UMKM;
            case KONSUMTIF -> MAX_AMOUNT_KONSUMTIF;
        };
    }

    public static int getMaxTenor(LoanType loanType){
        return switch (loanType){
            case UMKM  -> MAX_TENOR_UMKM;
            case KONSUMTIF -> MAX_TENOR_KONSUMTIF;
        };
    }
}
