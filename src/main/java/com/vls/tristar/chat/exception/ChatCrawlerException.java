package com.vls.tristar.chat.exception;

import com.vls.tristar.chat.common.ErrorCode;
import lombok.Getter;

@Getter
public class ChatCrawlerException extends RuntimeException {

    private final ErrorCode errorCode;

    public ChatCrawlerException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ChatCrawlerException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}
