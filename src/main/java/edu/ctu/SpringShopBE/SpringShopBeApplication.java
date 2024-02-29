package edu.ctu.SpringShopBE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan(basePackages = "edu.ctu.SpringShopBE.entity")
@EnableTransactionManagement
public class SpringShopBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringShopBeApplication.class, args);
	}

}
