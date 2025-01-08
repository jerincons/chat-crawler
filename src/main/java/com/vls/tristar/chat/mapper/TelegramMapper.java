package com.vls.tristar.chat.mapper;

import com.vls.tristar.chat.model.domain.TelegramData;
import com.vls.tristar.chat.model.telegram.Chat;
import com.vls.tristar.chat.model.telegram.Message;
import com.vls.tristar.chat.model.telegram.Update;
import com.vls.tristar.chat.model.telegram.User;

import java.time.Instant;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

public class TelegramMapper {

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
                .map(TelegramMapper::extractUpdateMessage)
                .map(Message::getMessageId)
                .orElse(-1L);
    }

    private static long extractChatId(Update update) {
        return ofNullable(update)
                .map(TelegramMapper::extractUpdateMessage)
                .map(Message::getChat)
                .map(Chat::getId)
                .orElse(-1L);
    }

    private static String extractType(Update update) {
        return ofNullable(update)
                .map(TelegramMapper::extractUpdateMessage)
                .map(Message::getChat)
                .map(Chat::getType)
                .orElse(null);
    }

    private static long extractUserId(Update update) {
        return ofNullable(update)
                .map(TelegramMapper::extractUpdateMessage)
                .map(Message::getFrom)
                .map(User::getId)
                .orElse(-1L);
    }

    private static String extractUsername(Update update) {
        return ofNullable(update)
                .map(TelegramMapper::extractUpdateMessage)
                .map(Message::getFrom)
                .map(User::getUsername)
                .orElse(null);
    }

    private static String extractFirstName(Update update) {
        return ofNullable(update)
                .map(TelegramMapper::extractUpdateMessage)
                .map(Message::getFrom)
                .map(User::getFirstName)
                .orElse(null);
    }

    private static String extractLastName(Update update) {
        return ofNullable(update)
                .map(TelegramMapper::extractUpdateMessage)
                .map(Message::getFrom)
                .map(User::getLastName)
                .orElse(null);
    }

    private static String extractMessage(Update update) {
        return ofNullable(update)
                .map(TelegramMapper::extractUpdateMessage)
                .map(Message::getText)
                .orElse(null);
    }

    private static String extractLang(Update update) {
        return ofNullable(update)
                .map(TelegramMapper::extractUpdateMessage)
                .map(Message::getFrom)
                .map(User::getLanguageCode)
                .orElse(null);
    }

    private static Instant extractDate(Update update) {
        return ofNullable(update)
                .map(TelegramMapper::extractUpdateMessage)
                .map(Message::getDate)
                .map(Instant::from)
                .orElse(null);
    }
}
