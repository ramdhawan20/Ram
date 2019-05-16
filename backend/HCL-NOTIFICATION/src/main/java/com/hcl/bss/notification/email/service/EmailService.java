package com.hcl.bss.notification.email.service;

public interface EmailService {

	void emailSubscriptionDetail(String subId, String eventType) throws Exception;
}
