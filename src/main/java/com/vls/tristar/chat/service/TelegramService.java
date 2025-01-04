package com.vls.tristar.chat.service;

import com.vls.tristar.chat.config.ChatConfig;
import com.vls.tristar.chat.model.telegram.GetUpdatesResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramService {

    private final WebClient telegramWebClient;
    private final ChatConfig chatConfig;
    private static final String GET_UPDATES_ENDPOINT = "/bot%s/getUpdates";

    public Mono<GetUpdatesResponse> getChatUpdates() {
        String url = String.format(GET_UPDATES_ENDPOINT, chatConfig.getProviders().getTelegram().getToken());
        return this.telegramWebClient
                .get()
                .uri(url)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        res -> {
                            log.error("Received 4xx status {}", res);
                            return Mono.error(new RuntimeException("4xx Error occurred"));
                        })
                .onStatus(HttpStatusCode::is5xxServerError,
                        res -> {
                            log.error("Received 5xx status {}", res);
                            return Mono.error(new RuntimeException("5xx Error occurred"));
                        })
                .bodyToMono(GetUpdatesResponse.class)
                .onErrorResume(e -> {
                    log.error("Failed to send request to url: {}", url, e);
                    return Mono.empty();
                });
    }
}
