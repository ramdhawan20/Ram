package com.hcl.bss.controllers;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import com.hcl.bss.schedulers.NotificationScheduler;
import com.hcl.bss.schedulers.SubscriptionRenewalScheduler;
import com.hcl.bss.schedulers.SubscriptionScheduler;

/**
 * This class should be used when the application is deployed on external Tomcat
 * web container
 * 
 * @author aditya-gu
 *
 */

@SpringBootApplication
@EnableJpaRepositories("com.hcl.bss.repository")
@EnableJpaAuditing
@EnableScheduling
@ComponentScan("com.hcl.bss.*")
public class SsRestApplication extends SpringBootServletInitializer implements CommandLineRunner {

	@Autowired
	SubscriptionScheduler subscriptionScheduler;

	@Autowired
	SubscriptionRenewalScheduler subscriptionRenewalScheduler;
	@Autowired
	NotificationScheduler notificationScheduler;

	public static void main(String[] args) {
		SpringApplication.run(SsRestApplication.class, args);
		// BCryptPasswordEncoder enc = new BCryptPasswordEncoder();
		// System.out.println("############"+enc.encode("admin")+"##########");
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(SsRestApplication.class);
	}

	@Override
	public void run(String... arg0) throws Exception {
		subscriptionScheduler.runSubscriptionBatch();
		subscriptionRenewalScheduler.runAutorenewSubscriptionsScheduler();
		//notificationScheduler.runSubscriptionDetails();
		 
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
