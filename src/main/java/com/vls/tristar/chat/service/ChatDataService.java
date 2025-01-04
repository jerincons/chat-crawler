package com.vls.tristar.chat.service;

import com.vls.tristar.chat.model.domain.ChatData;
import com.vls.tristar.chat.repository.ChatDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChatDataService {

    private final ChatDataRepository chatDataRepository;

    public void saveAllChats(Set<ChatData> chats) {
        chatDataRepository.saveAll(chats);
    }
}
