package com.vls.tristar.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vls.tristar.chat.model.domain.ChatData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private static final String FILE_PATH = "telegram_chat.json";
    private final ObjectMapper objectMapper;

    public Set<ChatData> getAllChats() throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return Set.of();
        }
        return objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(Set.class, ChatData.class));
    }

    public void saveAllChats(Set<ChatData> chats) throws IOException {
        objectMapper.writeValue(new File(FILE_PATH), chats);
    }
}
