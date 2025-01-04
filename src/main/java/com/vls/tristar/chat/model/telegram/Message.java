package com.vls.tristar.chat.model.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @JsonProperty("message_id")
    private long messageId;
    private User from;
    private Chat chat;
    private Instant date;
    private String text;
}
