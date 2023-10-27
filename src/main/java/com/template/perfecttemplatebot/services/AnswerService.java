package com.template.perfecttemplatebot.services;

import com.template.perfecttemplatebot.bot.TelegramBot;
import com.template.perfecttemplatebot.cash.Memory;
import com.template.perfecttemplatebot.templates.Keyboard;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.GetMe;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import static com.template.perfecttemplatebot.app_config.ApplicationContextProvider.getApplicationContext;

@Component
@Slf4j
public class AnswerService {

    private final Memory memory;

    public AnswerService(Memory memory) {
        this.memory = memory;
    }

    public BotApiMethod<?> drawKeyBoardWithMsg(long userId, Keyboard keyBoard) {
        SendMessage replyMessage = new SendMessage();
        replyMessage.setChatId(String.valueOf(userId));
        replyMessage.setText(keyBoard.getKeyBoardDescription());
        replyMessage.setReplyMarkup(keyBoard.getReplyKeyboard());
        return replyMessage;
    }

    public BotApiMethod<?> editKeyBoardWithMsg(long userId, Keyboard keyBoard, Integer messageId) {
        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
        editMessageReplyMarkup.setChatId(String.valueOf(userId));
        editMessageReplyMarkup.setMessageId(messageId);
        editMessageReplyMarkup.setReplyMarkup((InlineKeyboardMarkup) keyBoard.getReplyKeyboard());
        return editMessageReplyMarkup;
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

    @SneakyThrows
    public void sendTextRightNow(long userId, String text) {
        TelegramBot bot = (TelegramBot) getApplicationContext().getAutowireCapableBeanFactory().getBean("springWebhookBot");
        bot.execute(sendText(userId, text));
    }

    @SneakyThrows
    public void deleteAllMessages(long userId, Message message) {
        TelegramBot bot = (TelegramBot) getApplicationContext().getAutowireCapableBeanFactory().getBean("springWebhookBot");
        Integer messageId = message.getMessageId();
        do {
            try {
                if (!messageId.equals(memory.getMainMessageId()) && !messageId.equals(memory.getMainMenuMessageId())) {
                    bot.execute(DeleteMessage.builder()
                            .chatId(userId)
                            .messageId(messageId)
                            .build());
                }
            } catch (Throwable t) {
                bot.execute(GetMe.builder()
                        .build());
                break;
            }
            messageId--;
        } while (true);
    }
}

