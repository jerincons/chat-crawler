package com.vls.tristar.chat.scheduler;

import com.vls.tristar.chat.common.ErrorCode;
import com.vls.tristar.chat.exception.ChatCrawlerException;
import com.vls.tristar.chat.mapper.TelegramMapper;
import com.vls.tristar.chat.model.domain.TelegramData;
import com.vls.tristar.chat.model.telegram.GetUpdatesResponse;
import com.vls.tristar.chat.model.telegram.Update;
import com.vls.tristar.chat.service.TelegramDataService;
import com.vls.tristar.chat.service.api.TelegramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatScheduler {

    private final TelegramService telegramService;
    private final TelegramDataService telegramDataService;

    @Scheduled(fixedRateString = "${tristar.chat.schedule.rate:10000}")
    public void processTelegramChats() {
        log.info("Process started");
        telegramService
                .getChatUpdates()
                .log()
                .filter(Objects::nonNull)
                .map(GetUpdatesResponse::getResult)
                .subscribe(this::processUpdates);
    }

    private void processUpdates(List<Update> updates) {
        if (isEmpty(updates)) {
            log.warn("updates are empty for processing");
            return;
        }
        Set<TelegramData> chats = updates
                .stream()
                .parallel()
                .map(TelegramMapper::toTelegramData)
                .collect(Collectors.toSet());
        try {
            telegramDataService.saveAllChats(chats);
        } catch (Exception e) {
            log.error("Failed to save chats to DB", e);
            throw new ChatCrawlerException(e.getMessage(), e, ErrorCode.GENERIC_ERROR);
        }
    }
}
