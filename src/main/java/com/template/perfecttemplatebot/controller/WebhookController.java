package com.template.perfecttemplatebot.controller;

import com.template.perfecttemplatebot.bot.TelegramBot;
import com.template.perfecttemplatebot.templates.KeyBoardTemplates;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.annotation.PostConstruct;

import static com.template.perfecttemplatebot.app_config.ApplicationContextProvider.getApplicationContext;

@RestController
public class WebhookController {
    private final TelegramBot telegramBot;
    @Value("${telegrambot.adminId}")
    int adminId;
    @Value("${telegrambot.webHookPath}")
    String webHookPath;

    public WebhookController(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    // point for message
    @PostMapping("/")
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return telegramBot.onWebhookUpdateReceived(update);
    }

    @GetMapping
    public ResponseEntity get() {
        return ResponseEntity.ok().build();
    }

    @PostConstruct
    @SneakyThrows
    //after every restart app
    private void afterStart() {
        SetWebhook setWebhook = (SetWebhook) getApplicationContext().getAutowireCapableBeanFactory().getBean("setWebhookInstance");
        setWebhook.setDropPendingUpdates(true);
        setWebhook.setUrl(webHookPath);
        telegramBot.execute(setWebhook);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(adminId));
        sendMessage.setText("Произошла перезагрузка!");
        telegramBot.execute(sendMessage);
    }
}
