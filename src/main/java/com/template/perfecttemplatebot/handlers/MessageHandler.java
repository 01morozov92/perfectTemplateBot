package com.template.perfecttemplatebot.handlers;

import com.template.perfecttemplatebot.cash.BotStateCash;
import com.template.perfecttemplatebot.data_base.DAO.UserDAO;
import com.template.perfecttemplatebot.enums.BotState;
import com.template.perfecttemplatebot.service.AnswerService;
import com.template.perfecttemplatebot.templates.KeyBoardTemplates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class MessageHandler {

    private final UserDAO userDAO;
    private final KeyBoardTemplates keyBoardTemplates;
    private final BotStateCash botStateCash;
    private final AnswerService answerService;
    @Value("${telegrambot.adminId}")
    int adminId;

    public MessageHandler(UserDAO userDAO, KeyBoardTemplates keyBoardTemplates, BotStateCash botStateCash, AnswerService answerService) {
        this.userDAO = userDAO;
        this.keyBoardTemplates = keyBoardTemplates;
        this.botStateCash = botStateCash;
        this.answerService = answerService;
    }

    //обрабатывает апдейт в зависимости от назначенного ранее состояния бота
    public BotApiMethod<?> handle(Message message, BotState botState) {
        long userId = message.getFrom().getId();
        long chatId = message.getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        //if new user
        if (!userDAO.isExist(userId)) {
            userDAO.addNewUser(message, userId);
            botStateCash.saveBotState(userId, BotState.START);
        }
        //save state in to cache
        botStateCash.saveBotState(userId, botState);
        //if state =...
        switch (botState.name()) {
            case ("START"):
                return keyBoardTemplates.getMainMenuMessage("Воспользуйтесь главным меню", userId);
            case ("MENU_1"):
                //list events of user
                return answerService.drawKeyBoardWithMsg(userId, keyBoardTemplates.getFirstKeyboard(
                        false, userId, BotState.FIRST_KEYBOARD));
            case ("MENU_2"):
                //list events of user
                return answerService.mockHandler(userId);
            case ("MENU_3"):
                //list events of user
                return answerService.mockHandler(userId);
            case ("ADMIN_MENU_1"):
                //list events of user
                botStateCash.saveBotState(userId, BotState.START);
                StringBuilder stringBuilder = new StringBuilder();
                userDAO.findAllUsers().forEach(user -> stringBuilder.append(
                        user.getFirstName()).append("\n"));
                return SendMessage.builder()
                        .text(stringBuilder.toString())
                        .chatId(String.valueOf(chatId))
                        .build();
            case ("ADMIN_MENU_2"):
                //list events of user
                return answerService.mockHandler(userId);
            case ("SUB_MENU_1"):
                //list events of user
                return answerService.mockHandler(userId);
            case ("SUB_MENU_2"):
                //list events of user
                return answerService.mockHandler(userId);
            default:
                throw new IllegalStateException("Unexpected value: " + botState);
        }
    }
}
