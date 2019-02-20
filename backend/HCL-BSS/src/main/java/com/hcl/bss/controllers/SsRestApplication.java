package com.hcl.bss.controllers;

import com.hcl.bss.domain.Users;
import com.hcl.bss.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

//@SpringBootApplication (exclude=HibernateJpaAutoConfiguration.class)
	@SpringBootApplication
	@EnableJpaRepositories("com.hcl.bss.repository")
@ComponentScan("com.hcl.bss.*")
public class SsRestApplication implements CommandLineRunner {

	@Autowired
	UserServices userServices;


	public static void main(String[] args) {
		SpringApplication.run(SsRestApplication.class, args);
	}

	@Override
	public void run(String... arg0) throws IOException, URISyntaxException {
		Users users = this.userServices.findById(1);
		System.out.println("USer Details: " + users.getUserFirstName() + ", " + users.getUserLastName());

		List<Users> usersList = this.userServices.findByUserFirstName("aditya");
		usersList.forEach(user->
		{
			System.out.println(user.getId()+ " "+user.getUserFirstName()+" "+ user.getUserLastName()+" "+user.getPassword());
		});
	}

}

