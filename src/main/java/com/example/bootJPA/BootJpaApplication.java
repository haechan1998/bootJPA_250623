package com.example.bootJPA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // jpa 를 사용하기 위한 어노테이션
@SpringBootApplication
public class BootJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootJpaApplication.class, args);
	}

}
