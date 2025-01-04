package com.vls.tristar.chat.model.whatsapp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WhatsAppWebhookRequest {
    private String object;
    private List<Entry> entry;
}

