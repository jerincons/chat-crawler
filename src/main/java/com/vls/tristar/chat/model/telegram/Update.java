package com.vls.tristar.chat.model.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Update {
    @JsonProperty("update_id")
    private long updateId;
    private Message message;
    @JsonProperty("edited_message")
    private Message editedMessage;
}
