package com.p2plending.notification;

public interface EmailService {
    void send(String to, String subject, String message);
}
