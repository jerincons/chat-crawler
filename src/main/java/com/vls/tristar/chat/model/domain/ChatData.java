package com.vls.tristar.chat.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class ChatData {
    @Id
    private long id;
    private long messageId;
    private long chatId;
    private String type;
    private long userId;
    private String username;
    private String firstName;
    private String lastName;
    private String message;
    private String lang;
    private Instant date;
}
