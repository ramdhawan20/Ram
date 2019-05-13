package com.hcl.bss.notification.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "tb_subscription_notification_status")
@EntityListeners(AuditingEntityListener.class)
public class SubscriptionNotification implements Serializable{
	@Column(name = "UIDPK")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "uidpk_sequence")
	@TableGenerator(name = "uidpk_sequence", table = "id_gen", pkColumnName = "gen_name", valueColumnName = "gen_val", initialValue = 1000000000, allocationSize = 1)
	private Long id;
	@Column(name = "SUBSCRIPTION_ID")
	private String subscriptionId;
	@Column(name = "SUB_EMAIL_STATUS")
	private Character emailStatus;

	@Column(name = "SUB_SMS_STATUS")
	private Character smsStatus;

	@Column(name = "SUB_EVENT")
	private String subscriptionEvent;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public Character getEmailStatus() {
		return emailStatus;
	}

	public void setEmailStatus(Character emailStatus) {
		this.emailStatus = emailStatus;
	}

	public Character getSmsStatus() {
		return smsStatus;
	}

	public void setSmsStatus(Character smsStatus) {
		this.smsStatus = smsStatus;
	}

	public String getSubscriptionEvent() {
		return subscriptionEvent;
	}

	public void setSubscriptionEvent(String subscriptionEvent) {
		this.subscriptionEvent = subscriptionEvent;
	}

}
