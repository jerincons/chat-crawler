package com.vls.tristar.chat.service;

import com.vls.tristar.chat.model.domain.TelegramData;
import com.vls.tristar.chat.repository.TelegramDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class TelegramDataService {

    private final TelegramDataRepository telegramDataRepository;

    public void saveAllChats(Set<TelegramData> chats) {
        telegramDataRepository.saveAll(chats);
    }
}
