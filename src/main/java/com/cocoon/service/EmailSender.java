package com.cocoon.service;

public interface EmailSender {

    void sendEmailWithAttachment(String from, String to, String subject, String body, byte[] content);
}
