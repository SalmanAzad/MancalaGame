
package com.bol.mancala.game;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
@EnableCaching
@EnableDiscoveryClient
@SpringBootApplication
public class MancalaGameApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MancalaGameApiApplication.class, args);
	}
}
