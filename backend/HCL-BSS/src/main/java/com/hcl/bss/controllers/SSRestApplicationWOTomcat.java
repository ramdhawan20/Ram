/*package com.hcl.bss.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import com.hcl.bss.schedulers.BillingInvoiceScheduler;
import com.hcl.bss.schedulers.NotificationScheduler;
import com.hcl.bss.schedulers.SubscriptionRenewalScheduler;
import com.hcl.bss.schedulers.SubscriptionScheduler;

*//**
 * This class should be used when the application is deployed as executable jar
 * 
 * @author aditya-gu
 *
 *//*

@SpringBootApplication
@EnableJpaRepositories("com.hcl.bss.repository")
@EnableJpaAuditing
@EnableScheduling
@ComponentScan("com.hcl.bss.*")
public class SSRestApplicationWOTomcat implements CommandLineRunner {

	@Autowired
	SubscriptionScheduler subscriptionScheduler;

	@Autowired
	SubscriptionRenewalScheduler subscriptionRenewalScheduler;

	@Autowired
	BillingInvoiceScheduler billingInvoiceScheduler;
	@Autowired
	NotificationScheduler notificationScheduler;

	public static void main(String[] args) {
		SpringApplication.run(SSRestApplicationWOTomcat.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		subscriptionScheduler.runSubscriptionBatch();
		// billingInvoiceScheduler.updateBillingInvoice();
		subscriptionRenewalScheduler.runAutorenewSubscriptionsScheduler();
		// notificationScheduler.runSubscriptionDetails();
	}
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
*/