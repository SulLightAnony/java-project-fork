package com.p2plending.notification;

public interface FundingEventPublisher {
    void publishFundingCompleted(String loanId);
}
