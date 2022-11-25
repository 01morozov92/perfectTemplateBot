package com.template.perfecttemplatebot.service;

import com.template.perfecttemplatebot.cash.BotStateCash;
import com.template.perfecttemplatebot.data_base.DAO.UserDAO;
import com.template.perfecttemplatebot.data_base.entity.User;
import com.template.perfecttemplatebot.enums.BotState;
import com.template.perfecttemplatebot.templates.KeyBoardTemplates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class DrawService {
    private final UserDAO userDAO;
    private final BotStateCash botStateCash;

    @Autowired
    public DrawService(UserDAO userDAO, BotStateCash botStateCash) {
        this.userDAO = userDAO;
        this.botStateCash = botStateCash;
    }

    public BotApiMethod<?> drawKeyBoardWithMsg(long userId, KeyBoardTemplates.KeyBoard keyBoard) {
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

    public BotApiMethod<?> mockHandler(long userId, String msg) {
        SendMessage replyMessage = new SendMessage();
        replyMessage.setChatId(String.valueOf(userId));
        replyMessage.setText(msg);
        return replyMessage;
    }

    public SendMessage saveNewUser(Message message, long userId, SendMessage sendMessage) {
        String userName = message.getFrom().getUserName();
        User user = new User();
        user.setId(userId);
        user.setName(userName);
        user.setAmountOfDays(0);
        userDAO.save(user);
        sendMessage.setText("");
        botStateCash.saveBotState(userId, BotState.START);
        return sendMessage;
    }
}
