package com.vls.tristar.chat.service;

import com.vls.tristar.chat.model.domain.WhatsAppData;
import com.vls.tristar.chat.repository.WhatsAppDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WhatsAppService {

    private final WhatsAppDataRepository whatsAppDataRepository;

    public void save(WhatsAppData data) {
        whatsAppDataRepository.save(data);
    }

    public void saveAll(List<WhatsAppData> data) {
        whatsAppDataRepository.saveAll(data);
    }
}
