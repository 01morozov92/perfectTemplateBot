package com.template.perfecttemplatebot.handlers;

import com.template.perfecttemplatebot.DAO.UserDAO;
import com.template.perfecttemplatebot.cash.BotStateCash;
import com.template.perfecttemplatebot.entity.User;
import com.template.perfecttemplatebot.enums.BotState;
import com.template.perfecttemplatebot.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class SimpleHandler {

    private final MenuService menuService;
    private final UserDAO userDAO;
    private final BotStateCash botStateCash;

    @Autowired
    public SimpleHandler(MenuService menuService, UserDAO userDAO, BotStateCash botStateCash) {
        this.menuService = menuService;
        this.userDAO = userDAO;
        this.botStateCash = botStateCash;
    }

    public BotApiMethod<?> drawFirstKeyBoardWithMsg(long userId) {
        SendMessage replyMessage = new SendMessage();
        replyMessage.setChatId(String.valueOf(userId));
        replyMessage.setText("Это первая клавиатура");
        replyMessage.setReplyMarkup(menuService.getFirstKeyBoard());
        return replyMessage;
    }

    public BotApiMethod<?> drawSecondKeyBoardWithMsg(long userId) {
        SendMessage replyMessage = new SendMessage();
        replyMessage.setChatId(String.valueOf(userId));
        replyMessage.setText("Это вторая клавиатура");
        replyMessage.setReplyMarkup(menuService.getSecondKeyBoard());
        return replyMessage;
    }

    public BotApiMethod<?> drawThirdKeyBoardWithMsg(long userId) {
        SendMessage replyMessage = new SendMessage();
        replyMessage.setChatId(String.valueOf(userId));
        replyMessage.setText("Это третья клавиатура");
        replyMessage.setReplyMarkup(menuService.getThirdKeyBoard());
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
        userDAO.save(user);
        sendMessage.setText("");
        botStateCash.saveBotState(userId, BotState.START);
        return sendMessage;
    }
}
