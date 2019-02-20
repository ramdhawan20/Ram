package com.hcl.bss.controllers;

import com.hcl.bss.domain.Users;
import com.hcl.bss.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;
import java.net.URISyntaxException;

    @SpringBootApplication (exclude=HibernateJpaAutoConfiguration.class)
@ComponentScan("com.hcl.bss.*")
public class SsRestApplication implements CommandLineRunner {

	@Autowired UserServices userServices;


	public static void main(String[] args) {
		SpringApplication.run(SsRestApplication.class, args);
	}

	@Override
	public void run(String... arg0) throws IOException, URISyntaxException {
		/*Users users = this.userServices.findById(1);
		System.out.println("USer Details: " + users.getUserFirstName() + ", " + users.getUserLastName());*/
	}

}

