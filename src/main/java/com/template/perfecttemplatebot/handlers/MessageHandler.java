package com.template.perfecttemplatebot.handlers;

import com.template.perfecttemplatebot.cash.BotStateCash;
import com.template.perfecttemplatebot.data_base.DAO.UserDAO;
import com.template.perfecttemplatebot.data_base.entity.User;
import com.template.perfecttemplatebot.enums.BotState;
import com.template.perfecttemplatebot.service.AnswerService;
import com.template.perfecttemplatebot.templates.KeyBoardTemplates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.Serializable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    //обработка текстового сообщения и установка соответствующего состояния бота
    public BotApiMethod<?> handleInputMessage(Message message) {
        long userId = message.getFrom().getId();
        BotState botState;
        String inputMsg = message.getText();
        //we process messages of the main menu and any other messages
        //set state
        switch (inputMsg) {
            case "/start":
                botState = BotState.START;
                break;
            case "Осталось тренировок":
                botState = BotState.AMOUNT_OF_DAYS;
                break;
            case "Списать тренировку":
                botState = BotState.REMOVE_ONE_DAY;
                break;
            case "Добавить тренировку":
                botState = BotState.ADD_ONE_DAY;
                break;
            case "Меню3":
                botState = BotState.MENU_3;
                break;
            case "Все подписки":
                if (message.getFrom().getId() == adminId)
                    botState = BotState.ALL_SUBSCRIPTIONS;
                else botState = BotState.START;
                break;
            case "Действующие подписки":
                if (message.getFrom().getId() == adminId)
                    botState = BotState.LIST_OF_PAYED_SUBSCRIPTIONS;
                else botState = BotState.START;
                break;
            case "Истекающие подписки":
                if (message.getFrom().getId() == adminId)
                    botState = BotState.LIST_OF_EXPIRING_SUBSCRIPTIONS;
                else botState = BotState.START;
                break;
            case "Просроченные подписки":
                if (message.getFrom().getId() == adminId)
                    botState = BotState.LIST_OF_EXPIRED_SUBSCRIPTIONS;
                else botState = BotState.START;
                break;
            case "Продлить подписку":
                if (message.getFrom().getId() == adminId)
                    botState = BotState.RENEW_SUBSCRIPTION;
                else botState = BotState.START;
                break;
            case "Ожидающие подтверждения":
                if (message.getFrom().getId() == adminId)
                    botState = BotState.CHECK_WAITING_ROOM;
                else botState = BotState.START;
                break;
            default:
                botState = botStateCash.getBotStateMap().get(message.getFrom().getId()) == null ?
                        BotState.START : botStateCash.getBotStateMap().get(message.getFrom().getId());
        }
        //we pass the corresponding state to the handler
        //the corresponding method will be called
        return handle(message, botState, userId);
    }

    //обрабатывает апдейт в зависимости от назначенного ранее состояния бота
    public BotApiMethod<?> handle(Message message, BotState botState, long userId) {
        List<User> users;
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(userId));
        //Если новый пользователь
        if (!userDAO.isExist(userId)) {
            userDAO.addNewUser(message, userId);
            botState = botStateCash.saveBotState(userId, BotState.ENTRANCE);
        } else { //Если пользователь уже существует
            //Пользователь уже оплатил подписку
            if (userDAO.isSubscriber(userId)) {
                botStateCash.saveBotState(userId, botState);
            } else { //Пользователь не оплатил подписку
                //Пользователь уже представился
                if (userDAO.hasName(userId)) {
                    botState = botStateCash.saveBotState(userId, BotState.WAITING_ROOM);
                } else { //Пользователь еще не представился
                    botState = botStateCash.saveBotState(userId, BotState.AUTH);
                }
            }
        }
        //save state in to cache
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
                        userDAO.saveUser(message, userId, userData[0], userData[1], true);
                        return keyBoardTemplates.getMainMenuMessage("Воспользуйтесь главным меню", userId);
                    } else { //Пользователь не админ
                        botStateCash.saveBotState(userId, BotState.WAITING_ROOM);
                        userDAO.saveUser(message, userId, userData[0], userData[1], false);
                        return answerService.sendText(userId, "Необходимо подтверждение администратора. Можете написать ему в telegram @morozilya");
                    }
                } else {//Пользователь представился неправильно
                    return answerService.sendText(userId,
                            "Пожалуйста попробуйте ещё раз ввести свои имя и фамилию в формате: Иванов Иван");
                }
            case ("WAITING_ROOM"):
                return answerService.sendText(userId, "Необходимо подтверждение администратора. Можете написать ему в telegram @morozilya");
            case ("START"):
                return keyBoardTemplates.getMainMenuMessage(
                        "Воспользуйтесь главным меню", userId);
            case ("AMOUNT_OF_DAYS"):
                botStateCash.saveBotState(userId, BotState.START);
                return answerService.sendText(userId, "У вас осталось " + userDAO.findByTelegramId(userId).getAmountOfDays().toString() + " неиспользованных тренировок");
            case ("MENU_3"):
                return answerService.mockHandler(userId);
            case ("ALL_SUBSCRIPTIONS"):
                botStateCash.saveBotState(userId, BotState.START);
                StringBuilder stringBuilder = new StringBuilder();
                userDAO.findAllUsers().forEach(user -> {
                    stringBuilder.append(user.getLastName()).append(" ");
                    stringBuilder.append(user.getFirstName()).append(" ");
                    stringBuilder.append(user.getTelegramTag()).append(" ");
                    stringBuilder.append("Осталось тренировок: ").append(user.getAmountOfDays()).append("\n");
                });
                return SendMessage.builder()
                        .text(stringBuilder.toString())
                        .chatId(String.valueOf(userId))
                        .build();
            case ("LIST_OF_PAYED_SUBSCRIPTIONS"):
                botStateCash.saveBotState(userId, BotState.START);
                users = userDAO.findAllUsers().stream().filter(user -> user.getAmountOfDays() > 0).collect(Collectors.toList());
                return printSubscriptions(users, userId);
            case ("LIST_OF_EXPIRING_SUBSCRIPTIONS"):
                botStateCash.saveBotState(userId, BotState.START);
                users = userDAO.findAllUsers().stream().filter(user -> user.getAmountOfDays() > 0 & user.getAmountOfDays() <= 2).collect(Collectors.toList());
                return printSubscriptions(users, userId);
            case ("LIST_OF_EXPIRED_SUBSCRIPTIONS"):
                botStateCash.saveBotState(userId, BotState.START);
                users = userDAO.findAllUsers().stream().filter(user -> user.getAmountOfDays() == 0).collect(Collectors.toList());
                return printSubscriptions(users, userId);
            case ("REMOVE_ONE_DAY"):
                return answerService.drawKeyBoardWithMsg(userId, keyBoardTemplates.getGroupsKeyboard(false, userId, BotState.GET_GROUP_FOR_REMOVE_ONE_DAY));
            case ("ADD_ONE_DAY"):
                return answerService.drawKeyBoardWithMsg(userId, keyBoardTemplates.getGroupsKeyboard(false, userId, BotState.GET_GROUP_FOR_ADD_ONE_DAY));
            case ("RENEW_SUBSCRIPTION"):
                return answerService.drawKeyBoardWithMsg(userId, keyBoardTemplates.getGroupsKeyboard(false, userId, BotState.GET_GROUP_FOR_RENEW_SUBSCRIPTION));
            case ("CHECK_WAITING_ROOM"):
                return answerService.drawKeyBoardWithMsg(userId, keyBoardTemplates.getSubscriptionKeyboard(false, userId, BotState.SET_GROUP, userDAO.findAllBySubscriber(false)));
            case ("SET_DAYS"):
                return answerService.drawKeyBoardWithMsg(userId, keyBoardTemplates.getAmountOfDaysKeyboard(false, userId, BotState.SET_DAYS));
            case ("SUB_MENU_2"):
                return answerService.mockHandler(userId);
            default:
                throw new IllegalStateException("Unexpected value: " + botState);
        }
    }

    private BotApiMethod<? extends Serializable> printSubscriptions(List<User> users, long userId) {
        if (!users.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            users.forEach(user -> {
                stringBuilder.append(user.getLastName()).append(" ");
                stringBuilder.append(user.getFirstName()).append(" ");
                stringBuilder.append(user.getTelegramTag()).append(" ");
                stringBuilder.append("Осталось тренировок: ").append(user.getAmountOfDays()).append("\n");
            });
            return answerService.sendText(userId, stringBuilder.toString());
        } else {
            return answerService.sendText(userId, "Нет пользователей");
        }
    }
}
