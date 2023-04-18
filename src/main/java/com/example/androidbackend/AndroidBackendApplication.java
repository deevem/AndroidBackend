package com.example.androidbackend;

import com.example.androidbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class AndroidBackendApplication {


	public static void main(String[] args) {
		SpringApplication.run(AndroidBackendApplication.class, args);
	}


}
