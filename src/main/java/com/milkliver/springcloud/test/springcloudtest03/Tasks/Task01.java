package com.milkliver.springcloud.test.springcloudtest03.Tasks;

import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableTask
public class Task01 {
	@Bean
	public CommandLineRunner commandLineRunner() {
		return args -> {
			System.out.println("test");
		};
	}
}
