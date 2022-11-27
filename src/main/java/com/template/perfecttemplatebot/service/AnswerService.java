package com.template.perfecttemplatebot.service;

import com.template.perfecttemplatebot.bot.TelegramBot;
import com.template.perfecttemplatebot.templates.KeyBoard;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

import static com.template.perfecttemplatebot.app_config.ApplicationContextProvider.getApplicationContext;

@Component
public class AnswerService {

    public BotApiMethod<?> drawKeyBoardWithMsg(long userId, KeyBoard keyBoard) {
        SendMessage replyMessage = new SendMessage();
        replyMessage.setChatId(String.valueOf(userId));
        replyMessage.setText(keyBoard.getKeyBoardDescription());
        replyMessage.setReplyMarkup(keyBoard.getReplyKeyboard());
        return replyMessage;
    }

    public BotApiMethod<?> mockHandler(long userId) {
        SendMessage replyMessage = new SendMessage();
        replyMessage.setChatId(String.valueOf(userId));
        replyMessage.setText("Это заглушка");
        return replyMessage;
    }

    public BotApiMethod<?> sendText(long userId, String text) {
        SendMessage replyMessage = new SendMessage();
        replyMessage.setChatId(String.valueOf(userId));
        replyMessage.setText(text);
        return replyMessage;
    }

    public BotApiMethod<?> mockHandler(long userId, String msg) {
        SendMessage replyMessage = new SendMessage();
        replyMessage.setChatId(String.valueOf(userId));
        replyMessage.setText(msg);
        return replyMessage;
    }

    @SneakyThrows
    public void sendTextRightNow(long userId, String text) {
        TelegramBot bot = (TelegramBot) getApplicationContext().getAutowireCapableBeanFactory().getBean("springWebhookBot");
        bot.execute(sendText(userId, text));
    }

    @SneakyThrows
    public void deleteAllMessages(long userId, List<Message> messages) {
        TelegramBot bot = (TelegramBot) getApplicationContext().getAutowireCapableBeanFactory().getBean("springWebhookBot");
        if (messages != null) {
            if (!messages.isEmpty()) {
                for (Message message : messages) {
                    bot.execute(DeleteMessage.builder()
                            .chatId(userId)
                            .messageId(message.getMessageId())
                            .build());
                }
            }
        }
    }
}
