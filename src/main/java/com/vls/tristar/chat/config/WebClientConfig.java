package com.vls.tristar.chat.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private final ChatConfig chatConfig;

    @Bean
    public WebClient telegramWebClient() {
        log.info("telegram url {}",chatConfig.getProviders().getTelegram().getUrl());
        return WebClient
                .builder()
                .baseUrl(chatConfig.getProviders().getTelegram().getUrl())
                .build();
    }
}
