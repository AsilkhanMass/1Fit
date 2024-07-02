package com.example.onefit.config;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@PropertySource("application.yml")
public class BotConfig {

    @Value("${bot.name}")
    String botName;
    @Value("${bot.key}")
    String botToken;
}
