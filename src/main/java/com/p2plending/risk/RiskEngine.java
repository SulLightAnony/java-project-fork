package com.p2plending.risk;

public interface RiskEngine {
    ValidationResult evaluate(ValidationContext context);
}