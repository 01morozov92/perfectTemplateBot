package com.template.perfecttemplatebot.service;

import com.template.perfecttemplatebot.bot.TelegramBot;
import com.template.perfecttemplatebot.templates.KeyBoard;
import com.template.perfecttemplatebot.templates.KeyBoardTemplates;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.template.perfecttemplatebot.app_config.ApplicationContextProvider.getApplicationContext;
import static com.template.perfecttemplatebot.handlers.MessageHandler.mainMenuMessageId;
import static com.template.perfecttemplatebot.handlers.TelegramFacade.mainMessageId;

@Component
@Slf4j
public class AnswerService {

    private final KeyBoardTemplates keyBoardTemplates;

    public AnswerService(KeyBoardTemplates keyBoardTemplates) {
        this.keyBoardTemplates = keyBoardTemplates;
    }

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

//    @SneakyThrows
//    public void deleteAllMessages(long userId, List<Message> messages) {
//        TelegramBot bot = (TelegramBot) getApplicationContext().getAutowireCapableBeanFactory().getBean("springWebhookBot");
//        if (messages != null) {
//            if (!messages.isEmpty()) {
//                for (Message message : messages) {
//                    bot.execute(DeleteMessage.builder()
//                            .chatId(userId)
//                            .messageId(message.getMessageId())
//                            .build());
//                }
//            }
//        }
//    }

    @SneakyThrows
    public void deleteAllMessages(long userId, Message message) {
        TelegramBot bot = (TelegramBot) getApplicationContext().getAutowireCapableBeanFactory().getBean("springWebhookBot");
        Integer messageId = message.getMessageId();
        do {
            try {
                if (!messageId.equals(mainMessageId) && !messageId.equals(mainMenuMessageId)) {
                    bot.execute(DeleteMessage.builder()
                            .chatId(userId)
                            .messageId(messageId)
                            .build());
                }
            } catch (Throwable t) {
                bot.execute(sendText(userId,
                        "\uD83C\uDFC0"));
                break;
            }
            messageId--;
        } while (true);

    }
}

