package proj.java.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.cache.annotation.EnableCaching;

@org.springframework.boot.autoconfigure.SpringBootApplication
@EnableCaching
public class SpringBootApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringBootApplication.class, args);
	}
}
