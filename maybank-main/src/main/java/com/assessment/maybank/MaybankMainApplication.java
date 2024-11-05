package com.assessment.maybank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.assessment.maybank.repo")
@ComponentScan(basePackages = {"com.assessment.maybank"})
public class MaybankMainApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaybankMainApplication.class, args);
	}

}
