package com.hcl.bss.notification.controller;

import java.io.IOException;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableJpaRepositories("com.hcl.notification.repository")
@EnableJpaAuditing
@EnableScheduling
@ComponentScan("com.hcl.bss.*")
//@ImportResource({"classpath:application-context.xml"})
/**
 * 
 * @author ranjankumar.y
 *
 */
public class NotificationApplication{
	 private static Logger log = LoggerFactory.getLogger(NotificationApplication.class);

	

	 public static void main(String[] args) throws Exception {
		 
	        SpringApplication.run(NotificationApplication.class, args);
	        
	    }
	 
	 @Bean
		public RestTemplate restTemplate() {
		    return new RestTemplate();
		}
	 
	    }

	   

