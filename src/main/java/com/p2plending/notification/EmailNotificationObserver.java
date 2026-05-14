package com.p2plending.domain.funding;

/**
 * Implementasi FundingObserver yang mengirim notifikasi via email
 * kepada borrower dan seluruh lender yang berkontribusi ketika
 * pinjaman fully funded.
 */
public class EmailNotificationObserver implements FundingObserver {

    private final EmailService emailService;

    public EmailNotificationObserver(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void onFullyFunded(Funding funding) {
        notifyBorrower(funding);
        notifyLenders(funding);
    }

    private void notifyBorrower(Funding funding) {
        emailService.send(
            resolveEmailFor(funding.getLoanId()),
            "Dana Pinjaman Anda Telah Terkumpul",
            buildBorrowerMessage(funding)
        );
    }

    private void notifyLenders(Funding funding) {
        for (String lenderId : funding.getContributingLenderIds()) {
            emailService.send(
                resolveEmailFor(lenderId),
                "Pinjaman yang Anda Danai Telah Penuh",
                buildLenderMessage(funding, lenderId)
            );
        }
    }

    private String buildBorrowerMessage(Funding funding) {
        return String.format(
            "Selamat! Dana untuk pinjaman %s sebesar %s telah terkumpul penuh. " +
            "Proses disbursement akan segera dilakukan.",
            funding.getLoanId(),
            funding.getTotalFunded()
        );
    }

    private String buildLenderMessage(Funding funding, String lenderId) {
        return String.format(
            "Pinjaman %s yang Anda danai sebesar %s telah mencapai target pendanaan. " +
            "Total kontribusi Anda: %s.",
            funding.getLoanId(),
            funding.getTotalFunded(),
            funding.getTotalContributionBy(lenderId)
        );
    }

    /**
     * Placeholder — pada implementasi nyata, resolve email dari UserRepository
     * berdasarkan loanId atau lenderId.
     */
    private String resolveEmailFor(String id) {
        return id.toLowerCase() + "@p2plending.com";
    }
}