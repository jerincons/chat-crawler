package com.vls.tristar.chat.common;

public enum ErrorCode {
    ACCESS_DENIED(403),
    WRONG_CREDENTIALS(400),
    USER_LOCKED(400),
    GENERIC_ERROR(500),
    ROLE_NOT_FOUND(400),
    ENTITY_ALREADY_EXISTS(409),
    WRONG_INPUT(400);

    public final int httpCode;

    ErrorCode(int httpCode) {
        this.httpCode = httpCode;
    }
}
