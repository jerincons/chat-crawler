package com.vls.tristar.chat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("tristar.chat")
@Data
public class ChatConfig {

    private Providers providers;

    @Data
    public static class Providers {
        private ProviderConfig telegram;
    }

    @Data
    public static class ProviderConfig {
        private String url;
        private String token;
    }
}
