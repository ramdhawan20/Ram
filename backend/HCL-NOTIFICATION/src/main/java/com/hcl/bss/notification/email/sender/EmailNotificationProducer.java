package com.hcl.bss.notification.email.sender;


import com.hcl.bss.notification.dto.CustomerDto;

public interface EmailNotificationProducer {
public void createMail(CustomerDto customer);
public void sendMail(String htmlContent, String subscriptionId, String toEmail);
}
