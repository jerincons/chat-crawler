package com.vls.tristar.chat.model.telegram;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Chat {
    private long id;
    private String firstName;
    private String lastName;
    private String username;
    private String type;
}