package com.thss.androidbackend.config;

import com.thss.androidbackend.repository.EventHandler.PostEventHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
//@EnableSwagger2
@EnableMongoRepositories(basePackages = "com.thss.androidbackend.repository")
class Config{

}
