package com.ogunkuade.employeemanagementsystem;


import com.ogunkuade.employeemanagementsystem.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
@EnableFeignClients
public class EmployeeManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeManagementSystemApplication.class, args);
	}




}
