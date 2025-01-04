package com.vls.tristar.chat.scheduler;

import com.vls.tristar.chat.mapper.ChatMapper;
import com.vls.tristar.chat.model.domain.TelegramData;
import com.vls.tristar.chat.model.telegram.GetUpdatesResponse;
import com.vls.tristar.chat.model.telegram.Update;
import com.vls.tristar.chat.service.TelegramDataService;
import com.vls.tristar.chat.service.FileService;
import com.vls.tristar.chat.service.TelegramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatScheduler {

    private final TelegramService telegramService;
    private final FileService fileService;
    private final TelegramDataService telegramDataService;

    @Scheduled(fixedRateString = "${tristar.chat.schedule.rate: 10000}")
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
        Set<TelegramData> transformed = updates
                .stream()
                .parallel()
                .map(ChatMapper::toTelegramData)
                .collect(Collectors.toSet());
        CompletableFuture<Void> writeToFileTask = saveToFile(transformed);
        CompletableFuture<Void> writeToDbTask = saveToDB(transformed);
        CompletableFuture.allOf(writeToFileTask, writeToDbTask).join();
    }

    @Async
    private CompletableFuture<Void> saveToDB(Set<TelegramData> chats) {
        try {
            telegramDataService.saveAllChats(chats);
        } catch (Exception e) {
            log.error("Failed to save chats to DB", e);
        }
        return CompletableFuture.completedFuture(null);
    }

    @Async
    private CompletableFuture<Void> saveToFile(Set<TelegramData> chats) {
        try {
            Set<TelegramData> existingChats = fileService.getAllChats();
            Map<Long, TelegramData> idChats = existingChats
                    .stream()
                    .collect(Collectors.toMap(TelegramData::getId, chatData -> chatData));
            Set<TelegramData> toBeSaved = chats
                    .stream()
                    .filter(chat -> !idChats.containsKey(chat.getId()))
                    .collect(Collectors.toSet());
            if (isEmpty(toBeSaved)) {
                log.warn("No chats to be saved");
                return CompletableFuture.completedFuture(null);
            }
            Set<TelegramData> finalData = new HashSet<>(existingChats);
            finalData.addAll(toBeSaved);
            fileService.saveAllChats(finalData);
        } catch (Exception e) {
            log.error("Failed to save chats to file", e);
        }
        return CompletableFuture.completedFuture(null);
    }
}
