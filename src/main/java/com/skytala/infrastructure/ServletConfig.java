package com.skytala.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ThreadLocalRandom;

@Configuration
public class ServletConfig {

    Integer port;

    public ServletConfig(@Value("${port}") String port) {
        if(port.equals("random"))
            this.port = ThreadLocalRandom.current().nextInt(1001, 9999);
        else
            this.port = Integer.parseInt(port);
    }

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return (container -> {
            System.out.println("===== PORT: "+port);
            container.setPort(port);
        });
    }
}