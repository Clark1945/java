package org.travel_journal.ecommerceconfigserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ECommerceConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ECommerceConfigServerApplication.class, args);
    }

}
