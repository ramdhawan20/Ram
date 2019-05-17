package com.hcl.bss.notification.email.service;

/**
 * @author ranjankumar.y
 *
 */
public interface EmailService {

	void emailSubscriptionDetail(String subId, String eventType) throws Exception;
}
