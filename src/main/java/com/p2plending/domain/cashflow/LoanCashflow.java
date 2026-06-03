package com.p2plending.domain.cashflow;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Composite dalam Composite Pattern.
 * Merepresentasikan total cashflow pinjaman yang terdiri dari beberapa komponen (anak).
 * getAmount() mengembalikan jumlah dari seluruh child secara rekursif.
 * Orang 3
 */
public class LoanCashflow implements Cashflow {

    private final String name;
    private final List<Cashflow> children = new ArrayList<>();

    public LoanCashflow(String name) {
        this.name = name;
    }

    public void add(Cashflow cashflow) {
        children.add(cashflow);
    }

    public void remove(Cashflow cashflow) {
        children.remove(cashflow);
    }

    public List<Cashflow> getChildren() {
        return Collections.unmodifiableList(children);
    }

    /**
     * Total amount = jumlah semua child (Leaf maupun Composite lain).
     */
    @Override
    public BigDecimal getAmount() {
        return children.stream()
                .map(Cashflow::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Type Composite selalu PRINCIPAL sebagai representasi keseluruhan.
     * Gunakan getChildren() untuk melihat rincian per tipe.
     */
    @Override
    public CashflowType getType() {
        return CashflowType.PRINCIPAL;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "LoanCashflow{name='" + name + "', total=" + getAmount() + ", children=" + children + "}";
    }
}