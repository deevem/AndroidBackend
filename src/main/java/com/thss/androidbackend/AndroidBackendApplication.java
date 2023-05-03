package com.thss.androidbackend;

import com.thss.androidbackend.model.document.User;
import com.thss.androidbackend.repository.UserRepository;
import com.thss.androidbackend.service.user.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.stereotype.Component;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class AndroidBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(AndroidBackendApplication.class, args);
	}
}
