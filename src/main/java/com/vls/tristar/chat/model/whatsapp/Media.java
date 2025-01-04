package com.vls.tristar.chat.model.whatsapp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Media {
    private String id;
    private String mimeType;
    private String url;
}
