package com.vls.tristar.chat.model.telegram;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetUpdatesResponse {
    private boolean ok;
    private List<Update> result;
}
