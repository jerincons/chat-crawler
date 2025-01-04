package com.vls.tristar.chat.repository;

import com.vls.tristar.chat.model.domain.TelegramData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TelegramDataRepository extends MongoRepository<TelegramData, Long> {
}
