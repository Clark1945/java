package org.travel_journal.ecommerceeurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ECommerceEurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ECommerceEurekaServerApplication.class, args);
    }

}
