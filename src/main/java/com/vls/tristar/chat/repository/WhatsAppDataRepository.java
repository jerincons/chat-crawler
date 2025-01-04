package com.vls.tristar.chat.repository;

import com.vls.tristar.chat.model.domain.WhatsAppData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WhatsAppDataRepository extends MongoRepository<WhatsAppData, String> {
}
