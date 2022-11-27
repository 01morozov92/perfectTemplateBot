package com.template.perfecttemplatebot.service;

import com.template.perfecttemplatebot.bot.TelegramBot;
import com.template.perfecttemplatebot.cash.BotStateCash;
import com.template.perfecttemplatebot.data_base.DAO.UserDAO;
import com.template.perfecttemplatebot.templates.KeyBoard;
import com.template.perfecttemplatebot.templates.KeyBoardTemplates;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static com.template.perfecttemplatebot.app_config.ApplicationContextProvider.getApplicationContext;

@Component
public class AnswerService {
    private final UserDAO userDAO;
    private final BotStateCash botStateCash;

    @Autowired
    public AnswerService(UserDAO userDAO, BotStateCash botStateCash) {
        this.userDAO = userDAO;
        this.botStateCash = botStateCash;
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
}