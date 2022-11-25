package com.template.perfecttemplatebot.handlers;

import com.template.perfecttemplatebot.data_base.DAO.UserDAO;
import com.template.perfecttemplatebot.cash.BotStateCash;
import com.template.perfecttemplatebot.enums.BotState;
import com.template.perfecttemplatebot.service.DrawService;
import com.template.perfecttemplatebot.templates.KeyBoardTemplates;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class MessageHandler {

    private final UserDAO userDAO;
    private final KeyBoardTemplates keyBoardTemplates;
    private final BotStateCash botStateCash;
    private final DrawService drawService;

    public MessageHandler(UserDAO userDAO, KeyBoardTemplates keyBoardTemplates, BotStateCash botStateCash, DrawService drawService) {
        this.userDAO = userDAO;
        this.keyBoardTemplates = keyBoardTemplates;
        this.botStateCash = botStateCash;
        this.drawService = drawService;
    }

    //обрабатывает апдейт в зависимости от назначенного ранее состояния бота
    public BotApiMethod<?> handle(Message message, BotState botState) {
        long userId = message.getFrom().getId();
        long chatId = message.getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        //if new user
        if (!userDAO.isExist(userId)) {
            return userDAO.saveNewUser(message, userId, sendMessage);
        }
        //save state in to cache
        botStateCash.saveBotState(userId, botState);
        //if state =...
        switch (botState.name()) {
            case ("START"):
                return keyBoardTemplates.getMainMenuMessage(message.getChatId(),
                        "Воспользуйтесь главным меню", userId);
            case ("AMOUNT_OF_DAYS"):
                return drawService.drawKeyBoardWithMsg(userId, keyBoardTemplates.getFirstKeyBoard());
            case ("REMOVE_ONE_DAY"):
                return drawService.mockHandler(userId);
            case ("MENU_3"):
                return drawService.mockHandler(userId);
            case ("LIST_OF_ALL_SUBSCRIPTIONS"):
                botStateCash.saveBotState(userId, BotState.START);
                StringBuilder stringBuilder = new StringBuilder();
                userDAO.findAllUsers().forEach(user -> stringBuilder.append(user.getName()).append("\n"));
                return SendMessage.builder()
                        .text(stringBuilder.toString())
                        .chatId(String.valueOf(chatId))
                        .build();
            case ("LIST_OF_PAYED_SUBSCRIPTIONS"):
                return drawService.mockHandler(userId);
            case ("LIST_OF_EXPIRING_SUBSCRIPTIONS"):
                return drawService.mockHandler(userId);
            case ("LIST_OF_EXPIRED_SUBSCRIPTIONS"):
                return drawService.mockHandler(userId);
            case ("ADD_NEW_SUBSCRIPTION"):
                return drawService.mockHandler(userId);
            case ("SUB_MENU_1"):
                return drawService.mockHandler(userId);
            case ("SUB_MENU_2"):
                return drawService.mockHandler(userId, "Все супер ГУД!");
            default:
                throw new IllegalStateException("Unexpected value: " + botState);
        }
    }
}
