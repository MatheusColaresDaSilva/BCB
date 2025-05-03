package com.bcb.bcb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BcbApplication {

	public static void main(String[] args) {
		SpringApplication.run(BcbApplication.class, args);
	}

}
