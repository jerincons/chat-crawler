package com.vls.tristar.chat.advice;

import com.vls.tristar.chat.exception.ChatCrawlerException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ChatCrawlerException.class)
    public ResponseEntity<String> handleError(HttpServletRequest req, ChatCrawlerException ex) {
        log.error("Request: {} raised: ", req.getRequestURL(), ex);
        return new ResponseEntity<>("Failed", HttpStatus.resolve(ex.getErrorCode().httpCode));
    }
}
