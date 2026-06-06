package com.p2plending.notification;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.p2plending.domain.funding.Funding;

@DisplayName("EmailNotificationObserver Tests")
class EmailNotificationObserverTest {

    private EmailService emailService;
    private EmailNotificationObserver observer;

    @BeforeEach
    void setUp() {
        emailService = mock(EmailService.class);
        observer = new EmailNotificationObserver(emailService);
    }

    @Test
    @DisplayName("Notifikasi email borrower terkirim saat fully funded")
    void shouldSendEmailToBorrowerOnFullyFunded() {
        Funding funding = new Funding("LOAN-001", new BigDecimal("5000000"));
        funding.addContribution("LENDER-A", new BigDecimal("5000000"));

        observer.onFullyFunded(funding);

        // Verifikasi email ke borrower terkirim
        verify(emailService, atLeastOnce()).send(
            eq("loan-001@p2plending.com"),
            eq("Dana Pinjaman Anda Telah Terkumpul"),
            contains("LOAN-001")
        );
    }

    @Test
    @DisplayName("Notifikasi email lender terkirim saat fully funded")
    void shouldSendEmailToLenderOnFullyFunded() {
        Funding funding = new Funding("LOAN-001", new BigDecimal("5000000"));
        funding.addContribution("LENDER-A", new BigDecimal("5000000"));

        observer.onFullyFunded(funding);

        verify(emailService, atLeastOnce()).send(
            eq("lender-a@p2plending.com"),
            eq("Pinjaman yang Anda Danai Telah Penuh"),
            contains("LOAN-001")
        );
    }

    @Test
    @DisplayName("Email terkirim ke semua lender yang berkontribusi")
    void shouldSendEmailToAllContributingLenders() {
        Funding funding = new Funding("LOAN-002", new BigDecimal("10000000"));
        funding.addContribution("LENDER-A", new BigDecimal("5000000"));
        funding.addContribution("LENDER-B", new BigDecimal("5000000"));

        observer.onFullyFunded(funding);

        // 1 borrower + 2 lenders = 3 email
        verify(emailService, times(3)).send(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("Total kontribusi lender tercantum dalam isi email")
    void shouldIncludeLenderContributionInEmailBody() {
        Funding funding = new Funding("LOAN-003", new BigDecimal("5000000"));
        funding.addContribution("LENDER-A", new BigDecimal("5000000"));

        ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);
        observer.onFullyFunded(funding);

        verify(emailService, atLeastOnce()).send(anyString(), anyString(), bodyCaptor.capture());
        boolean kontribusiTercantum = bodyCaptor.getAllValues().stream()
            .anyMatch(body -> body.contains("5000000"));
        assertTrue(kontribusiTercantum);
    }
}