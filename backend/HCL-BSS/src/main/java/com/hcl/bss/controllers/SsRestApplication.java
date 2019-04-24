package com.hcl.bss.controllers;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.hcl.bss.schedulers.SubscriptionRenewalScheduler;
import com.hcl.bss.schedulers.SubscriptionScheduler;

//@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
@SpringBootApplication
@EnableJpaRepositories("com.hcl.bss.repository")
@EnableJpaAuditing
@EnableScheduling
@ComponentScan("com.hcl.bss.*")
public class SsRestApplication extends SpringBootServletInitializer  {

	//implements CommandLineRunner
	@Autowired
	SubscriptionScheduler subscriptionScheduler;

	@Autowired
	SubscriptionRenewalScheduler subscriptionRenewalScheduler;

	/*@Autowired
	BillingInvoiceScheduler billingInvoiceScheduler;*/

	public static void main(String[] args) {
		SpringApplication.run(SsRestApplication.class, args);
	}

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SsRestApplication.class);
    }
	/*TO run SubscriptionBatch*/
	/*
	 * @Override public void run(String... arg0) throws IOException,
	 * URISyntaxException { //BCryptPasswordEncoder passwordEncoder = new
	 * BCryptPasswordEncoder();
	 * //System.out.println(passwordEncoder.encode("admin"));
	 * subscriptionScheduler.runSubscriptionBatch();
	 * //billingInvoiceScheduler.updateBillingInvoice();
	 * subscriptionRenewalScheduler.runAutorenewSubscriptionsScheduler(); }
	 */

}

