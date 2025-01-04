package com.vls.tristar.chat.controller;

import com.vls.tristar.chat.model.whatsapp.WhatsAppWebhookRequest;
import com.vls.tristar.chat.service.WhatsAppService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.vls.tristar.chat.mapper.ChatMapper.toWhatsAppData;

@Slf4j
@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class WebHookController {

    private final WhatsAppService whatsAppService;

    @PostMapping("/twilio")
    @ResponseStatus(HttpStatus.OK)
    public void saveTwilioMessage(@RequestParam("From") String from,
                                  @RequestParam("To") String to,
                                  @RequestParam("Body") String body,
                                  @RequestParam("MessageSid") String messageSid,
                                  @RequestParam("NumMedia") int numMedia) {
        log.info("Incoming twilio message, From: {}, To: {}, Body: {}, MessageSid: {}", from, to, body, messageSid);
        whatsAppService.save(toWhatsAppData(from, to, body, messageSid));
    }

    @PostMapping("/whatsapp")
    @ResponseStatus(HttpStatus.OK)
    public void saveWhatsAppMessage(@RequestBody WhatsAppWebhookRequest body,
                                    HttpServletRequest request) {
        log.info("Incoming meta message {}", body);
        whatsAppService.saveAll(toWhatsAppData(body));
    }
}
