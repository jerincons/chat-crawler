package com.vls.tristar.chat.repository;

import com.vls.tristar.chat.model.domain.ChatData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatDataRepository extends MongoRepository<ChatData, Long> {
}
