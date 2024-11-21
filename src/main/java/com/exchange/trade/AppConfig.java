package com.exchange.trade;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class AppConfig {
    Trade trade = new Trade();

    @Getter
    @Setter
    public static class Trade {
        int admin;
    }
}
