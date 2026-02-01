package org.spgomez.springcloud.microservice.grades;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MsGradesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsGradesApplication.class, args);
	}

}
