package com.template.perfecttemplatebot.handlers;

import com.template.perfecttemplatebot.cash.BotStateCash;
import com.template.perfecttemplatebot.data_base.DAO.UserDAO;
import com.template.perfecttemplatebot.enums.BotState;
import com.template.perfecttemplatebot.service.AnswerService;
import com.template.perfecttemplatebot.templates.KeyBoardTemplates;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MessageHandler {

    private final UserDAO userDAO;
    private final KeyBoardTemplates keyBoardTemplates;
    private final BotStateCash botStateCash;
    private final AnswerService answerService;

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
        botStateCash.saveBotState(userId, botState);
        //Если новый пользователь
        if (!userDAO.isExist(userId)) {
            userDAO.addNewUser(message, userId, sendMessage);
            botStateCash.saveBotState(userId, BotState.ENTRANCE);
        } else { //Если пользователь уже существует
            //Пользователь уже оплатил подписку
            if (userDAO.isSubscriber(userId)) {
                botStateCash.saveBotState(userId, BotState.START);
            } else { //Пользователь не оплатил подписку
                //Пользователь уже представился
                if (userDAO.hasName(userId)) {
                    botStateCash.saveBotState(userId, BotState.WAITING_ROOM);
                } else { //Пользователь еще не представился
                    botStateCash.saveBotState(userId, BotState.AUTH);
                }
            }
        }
        //save state in to cache
        botState = botStateCash.getBotStateMap().get(userId);
        switch (botState.name()) {
            case ("ENTRANCE"):
                if (userId == keyBoardTemplates.getAdmin_id()) {
                    botStateCash.saveBotState(userId, BotState.AUTH);
                    return answerService.sendText(userId, "Добрый день, администратор. Пожалуйста введите свои имя и фамилию в формате: Иванов Иван");
                } else {
                    botStateCash.saveBotState(userId, BotState.AUTH);
                    return answerService.sendText(userId, "Добрый день уважаемый гость. Пожалуйста введите свои имя и фамилию в формате: Иванов Иван");
                }
            case ("AUTH"):
                Pattern authPattern = Pattern.compile("^[а-яёА-ЯЁ]+\\s[а-яёА-ЯЁ]+$");
                Matcher matcher = authPattern.matcher(message.getText());
                String[] userData = message.getText().split(" ");
                //Пользователь представился правильно
                if (matcher.find()) {
                    //Пользователь является админом
                    if (userId == keyBoardTemplates.getAdmin_id()) {
                        botStateCash.saveBotState(userId, BotState.START);
                        return userDAO.saveUser(message, userId, sendMessage, userData[0], userData[1], true);
                    } else { //Пользователь не админ
                        botStateCash.saveBotState(userId, BotState.WAITING_ROOM);
                        return userDAO.saveUser(message, userId, sendMessage, userData[0], userData[1], false);
                    }
                } else {//Пользователь представился неправильно
                    return answerService.sendText(userId,
                            "Пожалуйста попробуйте ещё раз ввести свои имя и фамилию в формате: Иванов Иван");
                }
            case ("WAITING_ROOM"):
                return answerService.sendText(userId, "Необходимо подтверждение администратора. Можете написать ему в telegram @morozilya");
            case ("START"):
                return keyBoardTemplates.getMainMenuMessage(message.getChatId(),
                        "Воспользуйтесь главным меню", userId);
            case ("AMOUNT_OF_DAYS"):
                return answerService.drawKeyBoardWithMsg(userId, keyBoardTemplates.getFirstKeyBoard());
            case ("REMOVE_ONE_DAY"):
                return answerService.mockHandler(userId);
            case ("MENU_3"):
                return answerService.mockHandler(userId);
            case ("LIST_OF_ALL_SUBSCRIPTIONS"):
                botStateCash.saveBotState(userId, BotState.START);
                StringBuilder stringBuilder = new StringBuilder();
                userDAO.findAllUsers().forEach(user -> stringBuilder.append(user.getTelegramTag()).append("\n"));
                return SendMessage.builder()
                        .text(stringBuilder.toString())
                        .chatId(String.valueOf(chatId))
                        .build();
            case ("LIST_OF_PAYED_SUBSCRIPTIONS"):
                return answerService.mockHandler(userId);
            case ("LIST_OF_EXPIRING_SUBSCRIPTIONS"):
                return answerService.mockHandler(userId);
            case ("LIST_OF_EXPIRED_SUBSCRIPTIONS"):
                return answerService.mockHandler(userId);
            case ("ADD_NEW_SUBSCRIPTION"):
                return answerService.mockHandler(userId);
            case ("RENEW_SUBSCRIPTION"):
//                User user = userDAO.findByUserId(userId);
//                user.setAmountOfDays(user.getAmountOfDays() + 8);
//                userDAO.save(user);
                return answerService.mockHandler(userId);
            case ("SUB_MENU_1"):
                return answerService.mockHandler(userId);
            case ("SUB_MENU_2"):
                return answerService.mockHandler(userId, "Все супер ГУД!");
            default:
                throw new IllegalStateException("Unexpected value: " + botState);
        }
    }
}
