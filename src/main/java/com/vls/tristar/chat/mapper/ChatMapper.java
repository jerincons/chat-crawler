package com.vls.tristar.chat.mapper;

import com.vls.tristar.chat.common.ErrorCode;
import com.vls.tristar.chat.exception.ChatCrawlerException;
import com.vls.tristar.chat.model.domain.TelegramData;
import com.vls.tristar.chat.model.domain.WhatsAppData;
import com.vls.tristar.chat.model.telegram.Chat;
import com.vls.tristar.chat.model.telegram.Message;
import com.vls.tristar.chat.model.telegram.Update;
import com.vls.tristar.chat.model.telegram.User;
import com.vls.tristar.chat.model.whatsapp.Change;
import com.vls.tristar.chat.model.whatsapp.Entry;
import com.vls.tristar.chat.model.whatsapp.Metadata;
import com.vls.tristar.chat.model.whatsapp.Text;
import com.vls.tristar.chat.model.whatsapp.Value;
import com.vls.tristar.chat.model.whatsapp.WhatsAppWebhookRequest;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static java.util.function.Predicate.not;

public class ChatMapper {

    public static TelegramData toTelegramData(Update update) {
        return TelegramData
                .builder()
                .id(extractId(update))
                .messageId(extractMessageId(update))
                .chatId(extractChatId(update))
                .type(extractType(update))
                .userId(extractUserId(update))
                .username(extractUsername(update))
                .firstName(extractFirstName(update))
                .lastName(extractLastName(update))
                .message(extractMessage(update))
                .lang(extractLang(update))
                .date(extractDate(update))
                .build();
    }

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
                        .map(ChatMapper::toWhatsAppData)
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

    private static long extractId(Update update) {
        return ofNullable(update).map(Update::getUpdateId).orElse(-1L);
    }

    private static Message extractUpdateMessage(Update update) {
        if (update != null && update.getEditedMessage() != null) {
            return of(update)
                    .map(Update::getEditedMessage)
                    .orElse(null);
        }
        return ofNullable(update)
                .map(Update::getMessage)
                .orElse(null);
    }

    private static long extractMessageId(Update update) {
        return ofNullable(update)
                .map(ChatMapper::extractUpdateMessage)
                .map(Message::getMessageId)
                .orElse(-1L);
    }

    private static long extractChatId(Update update) {
        return ofNullable(update)
                .map(ChatMapper::extractUpdateMessage)
                .map(Message::getChat)
                .map(Chat::getId)
                .orElse(-1L);
    }

    private static String extractType(Update update) {
        return ofNullable(update)
                .map(ChatMapper::extractUpdateMessage)
                .map(Message::getChat)
                .map(Chat::getType)
                .orElse(null);
    }

    private static long extractUserId(Update update) {
        return ofNullable(update)
                .map(ChatMapper::extractUpdateMessage)
                .map(Message::getFrom)
                .map(User::getId)
                .orElse(-1L);
    }

    private static String extractUsername(Update update) {
        return ofNullable(update)
                .map(ChatMapper::extractUpdateMessage)
                .map(Message::getFrom)
                .map(User::getUsername)
                .orElse(null);
    }

    private static String extractFirstName(Update update) {
        return ofNullable(update)
                .map(ChatMapper::extractUpdateMessage)
                .map(Message::getFrom)
                .map(User::getFirstName)
                .orElse(null);
    }

    private static String extractLastName(Update update) {
        return ofNullable(update)
                .map(ChatMapper::extractUpdateMessage)
                .map(Message::getFrom)
                .map(User::getLastName)
                .orElse(null);
    }

    private static String extractMessage(Update update) {
        return ofNullable(update)
                .map(ChatMapper::extractUpdateMessage)
                .map(Message::getText)
                .orElse(null);
    }

    private static String extractLang(Update update) {
        return ofNullable(update)
                .map(ChatMapper::extractUpdateMessage)
                .map(Message::getFrom)
                .map(User::getLanguageCode)
                .orElse(null);
    }

    private static Instant extractDate(Update update) {
        return ofNullable(update)
                .map(ChatMapper::extractUpdateMessage)
                .map(Message::getDate)
                .map(Instant::from)
                .orElse(null);
    }
}
