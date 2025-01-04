package com.vls.tristar.chat.model.whatsapp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String from;
    private String id;
    private String timestamp;
    private Text text;
    private String type;
    private Media media;
}