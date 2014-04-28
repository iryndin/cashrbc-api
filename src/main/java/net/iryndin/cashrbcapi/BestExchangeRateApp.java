package net.iryndin.cashrbcapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
/*
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import java.util.concurrent.TimeUnit;
*/
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * Starter app
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
@EnableScheduling
public class BestExchangeRateApp {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(BestExchangeRateApp.class, args);
    }

    /*
    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        JettyEmbeddedServletContainerFactory factory = new JettyEmbeddedServletContainerFactory();
        factory.setPort(9000);
        factory.setSessionTimeout(10, TimeUnit.MINUTES);
        return factory;
    }
    */
}