package com.bit.ProjectApprovalSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class ProjectApprovalSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectApprovalSystemApplication.class, args);
	}

}
