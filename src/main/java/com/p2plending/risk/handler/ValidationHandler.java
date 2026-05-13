package com.p2plending.risk.handler;

import com.p2plending.risk.ValidationContext;
import com.p2plending.risk.ValidationResult;

public abstract class ValidationHandler {
    protected ValidationHandler next;

    public ValidationHandler setNext(ValidationHandler next) {
        this.next = next;
        return next;
    }

    public abstract ValidationResult handle(ValidationContext context);

    protected ValidationResult passToNext(ValidationContext context) {
        if (next != null) return next.handle(context);
        return ValidationResult.approve();
    }
}