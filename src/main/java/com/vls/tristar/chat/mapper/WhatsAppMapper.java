package com.vls.tristar.chat.mapper;

import com.vls.tristar.chat.common.ErrorCode;
import com.vls.tristar.chat.exception.ChatCrawlerException;
import com.vls.tristar.chat.model.domain.WhatsAppData;
import com.vls.tristar.chat.model.whatsapp.Change;
import com.vls.tristar.chat.model.whatsapp.Entry;
import com.vls.tristar.chat.model.whatsapp.Metadata;
import com.vls.tristar.chat.model.whatsapp.Text;
import com.vls.tristar.chat.model.whatsapp.Value;
import com.vls.tristar.chat.model.whatsapp.WhatsAppWebhookRequest;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static java.util.function.Predicate.not;

public class WhatsAppMapper {

    public static List<WhatsAppData> toWhatsAppData(WhatsAppWebhookRequest request) {
        return ofNullable(request)
                .map(WhatsAppWebhookRequest::getEntry)
                .filter(not(CollectionUtils::isEmpty))
                .orElseGet(Collections::emptyList)
                .stream()
                .map(Entry::getChanges)
                .filter(not(CollectionUtils::isEmpty))
                .flatMap(changes -> changes
                        .stream()
                        .map(WhatsAppMapper::toWhatsAppData)
                        .flatMap(Collection::stream))
                .toList();
    }

    private static List<WhatsAppData> toWhatsAppData(Change change) {
        return ofNullable(change)
                .map(c -> {
                    String to = extractTo(c);
                    return mapToWhatsAppDataList(c, to);
                })
                .orElse(null);
    }

    private static List<WhatsAppData> mapToWhatsAppDataList(Change c, String to) {
        return of(c)
                .map(Change::getValue)
                .map(Value::getMessages)
                .filter(not(CollectionUtils::isEmpty))
                .orElseGet(Collections::emptyList)
                .stream()
                .map(message -> toWhatsAppData(extractWhatsAppMessageFrom(message), to, extractWhatsAppMessageBody(message), getWhatsAppMessageId(message)))
                .toList();
    }

    private static String getWhatsAppMessageId(com.vls.tristar.chat.model.whatsapp.Message message) {
        return message != null ? message.getId() : null;
    }

    private static String extractWhatsAppMessageFrom(com.vls.tristar.chat.model.whatsapp.Message message) {
        return ofNullable(message)
                .map(com.vls.tristar.chat.model.whatsapp.Message::getFrom)
                .orElseThrow(() -> new ChatCrawlerException("Invalid from contact id", ErrorCode.WRONG_INPUT));
    }

    private static String extractWhatsAppMessageBody(com.vls.tristar.chat.model.whatsapp.Message message) {
        return ofNullable(message)
                .map(com.vls.tristar.chat.model.whatsapp.Message::getText)
                .map(Text::getBody)
                .orElse(null);
    }

    private static String extractTo(Change c) {
        return of(c)
                .map(Change::getValue)
                .map(Value::getMetadata)
                .map(Metadata::getDisplayPhoneNumber)
                .orElseThrow(() -> new ChatCrawlerException("Invalid to contact id", ErrorCode.WRONG_INPUT));
    }

    public static WhatsAppData toWhatsAppData(String from, String to, String body, String messageSid) {
        return WhatsAppData
                .builder()
                .messageId(messageSid)
                .from(from)
                .to(to)
                .message(body)
                .build();
    }
}
