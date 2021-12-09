package com.template.perfecttemplatebot.handlers;

import com.template.perfecttemplatebot.DAO.UserDAO;
import com.template.perfecttemplatebot.cash.BotStateCash;
import com.template.perfecttemplatebot.enums.BotState;
import com.template.perfecttemplatebot.service.MenuService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class MessageHandler {

    private final UserDAO userDAO;
    private final MenuService menuService;
    private final BotStateCash botStateCash;
    private final SimpleHandler simpleHandler;

    public MessageHandler(UserDAO userDAO, MenuService menuService, BotStateCash botStateCash, SimpleHandler simpleHandler) {
        this.userDAO = userDAO;
        this.menuService = menuService;
        this.botStateCash = botStateCash;
        this.simpleHandler = simpleHandler;
    }

    //обрабатывает апдейт в зависимости от назначенного ранее состояния бота
    public BotApiMethod<?> handle(Message message, BotState botState) {
        long userId = message.getFrom().getId();
        long chatId = message.getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        //if new user
        if (!userDAO.isExist(userId)) {
            return simpleHandler.saveNewUser(message, userId, sendMessage);
        }
        //save state in to cache
        botStateCash.saveBotState(userId, botState);
        //if state =...
        switch (botState.name()) {
            case ("START"):
                return menuService.getMainMenuMessage(message.getChatId(),
                        "Воспользуйтесь главным меню", userId);
            case ("MENU_1"):
                //list events of user
                return simpleHandler.drawFirstKeyBoardWithMsg(userId);
            case ("MENU_2"):
                //list events of user
                return simpleHandler.mockHandler(userId);
            case ("MENU_3"):
                //list events of user
                return simpleHandler.mockHandler(userId);
            case ("ADMIN_MENU_1"):
                //list events of user
                botStateCash.saveBotState(userId, BotState.START);
                StringBuilder stringBuilder = new StringBuilder();
                userDAO.findAllUsers().forEach(user -> stringBuilder.append(user.getName()).append("\n"));
                return SendMessage.builder()
                        .text(stringBuilder.toString())
                        .chatId(String.valueOf(chatId))
                        .build();
            case ("ADMIN_MENU_2"):
                //list events of user
                return simpleHandler.mockHandler(userId);
            case ("SUB_MENU_1"):
                //list events of user
                return simpleHandler.mockHandler(userId);
            case ("SUB_MENU_2"):
                //list events of user
                return simpleHandler.mockHandler(userId, "Все супер ГУД!");
            default:
                throw new IllegalStateException("Unexpected value: " + botState);
        }
    }
}
