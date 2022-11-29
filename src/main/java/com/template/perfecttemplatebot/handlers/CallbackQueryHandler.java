package com.template.perfecttemplatebot.handlers;

import com.template.perfecttemplatebot.cash.BotStateCash;
import com.template.perfecttemplatebot.cash.Memory;
import com.template.perfecttemplatebot.data_base.DAO.UserDAO;
import com.template.perfecttemplatebot.data_base.entity.User;
import com.template.perfecttemplatebot.enums.BotState;
import com.template.perfecttemplatebot.enums.Group;
import com.template.perfecttemplatebot.service.AnswerService;
import com.template.perfecttemplatebot.templates.KeyBoardTemplates;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CallbackQueryHandler {
    private final BotStateCash botStateCash;

    private final Memory memory;
    private final KeyBoardTemplates keyBoardTemplates;
    private final AnswerService answerService;
    private final UserDAO userDAO;
    private final Pattern telegramTagPatternt = Pattern.compile("^@[_a-zA-Z]+$");
    private BotApiMethod<?> callbackAnswer;
    private User user;

    @Autowired
    public CallbackQueryHandler(BotStateCash botStateCash, Memory memory, KeyBoardTemplates keyBoardTemplates, AnswerService answerService, UserDAO userDAO) {
        this.botStateCash = botStateCash;
        this.memory = memory;
        this.keyBoardTemplates = keyBoardTemplates;
        this.answerService = answerService;
        this.userDAO = userDAO;
    }

    @SneakyThrows
    public BotApiMethod<?> processCallbackQuery(CallbackQuery callbackQuery) {
        final long chatId = callbackQuery.getMessage().getChatId();
        final long userId = callbackQuery.getFrom().getId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        Message message = callbackQuery.getMessage();


        String data = callbackQuery.getData();
        switch (data) {
            case ("back") -> {
                callbackAnswer = answerService.drawKeyBoardWithMsg(userId, memory.getLastKeyboard(userId));
            }
            case ("back_to_main_menu") -> {
                callbackAnswer = answerService.sendText(userId, "Воспользуйтесь главным меню");
            }
            //выбор количества дней
            case ("8"), ("16"), ("24"), ("32") -> {
                callbackAnswer = promoteUser(userId, Integer.parseInt(data), message);
                botStateCash.saveBotState(userId, BotState.START);
            }
            case ("CHILDREN"), ("TEENAGER"), ("FEMALE_INDIVIDUAL"), ("ADULT") -> {
                if (botStateCash.getLastBotState().equals(BotState.SET_GROUP)) {
                    callbackAnswer = setGroup(userId, Group.valueOf(data));
                } else if (botStateCash.getLastBotState().equals(BotState.GET_GROUP_FOR_REMOVE_ONE_DAY)) {
                    memory.setLastGroup(data);
                    callbackAnswer = drawUsersFromGroup(userId, BotState.REMOVE_ONE_DAY, Group.valueOf(data));
                } else if (botStateCash.getLastBotState().equals(BotState.GET_GROUP_FOR_ADD_ONE_DAY)) {
                    memory.setLastGroup(data);
                    callbackAnswer = drawUsersFromGroup(userId, BotState.ADD_ONE_DAY, Group.valueOf(data));
                } else if (botStateCash.getLastBotState().equals(BotState.GET_GROUP_FOR_RENEW_SUBSCRIPTION)) {
                    memory.setLastGroup(data);
                    callbackAnswer = drawUsersFromGroup(userId, BotState.SET_DAYS, Group.valueOf(data));
                }
            }
            //вторая клавиатура
            case ("first_btn_second_menu") -> {
                callbackAnswer = answerService.editKeyBoardWithMsg(chatId, keyBoardTemplates.getThirdKeyBoard(true, userId, BotState.START), messageId);
            }
            case ("second_btn_second_menu") -> {
                callbackAnswer = new SendMessage(String.valueOf(chatId), "Вторая заглушка подменю 1");
                botStateCash.saveBotState(userId, BotState.START);
            }
            case ("third_btn_second_menu") -> {
                callbackAnswer = new SendMessage(String.valueOf(chatId), "Третья заглушка подменю 1");
                botStateCash.saveBotState(userId, BotState.START);
            }
            //третья клавиатура
            case ("first_btn_third_menu") -> {
                callbackAnswer = new SendMessage(String.valueOf(chatId), "Первая заглушка подменю 2");
                botStateCash.saveBotState(userId, BotState.SUB_MENU_2);
            }
            case ("second_btn_third_menu") -> {
                callbackAnswer = new SendMessage(String.valueOf(chatId), "Вторая заглушка подменю 2");
                botStateCash.saveBotState(userId, BotState.START);
            }
            case ("third_btn_third_menu") -> {
                callbackAnswer = new SendMessage(String.valueOf(chatId), "Третья заглушка подменю 2");
                botStateCash.saveBotState(userId, BotState.START);
            }
            default -> callbackAnswer = handleDynamicCallbacks(userId, data, messageId);
        }
        return callbackAnswer;
    }

    private BotApiMethod<?> handleDynamicCallbacks(long userId, String data, Integer messageId) {
        Matcher matcher = telegramTagPatternt.matcher(data);
        switch (botStateCash.getLastBotState()) {
            case REMOVE_ONE_DAY -> {
                matcher = telegramTagPatternt.matcher(data);
                if (matcher.find()) {
                    user = userDAO.findByTelegramTag(data);
                    int amountOfDays = user.getAmountOfDays();
                    if (amountOfDays > 0) {
                        user.setAmountOfDays(amountOfDays - 1);
                        userDAO.save(user);
                    } else {
                        answerService.sendTextRightNow(userId, "У участника: " + data + " уже закончились оплаченные тренировки");
                    }
                    callbackAnswer = answerService.editKeyBoardWithMsg(userId, keyBoardTemplates.getSubscriptionKeyboard(true, userId, BotState.REMOVE_ONE_DAY, userDAO.findAllByPersonGroup(memory.getLastGroup())), messageId);
                } else {
                    callbackAnswer = answerService.sendText(userId, "Такой участник: " + data + " не найден в базе");
                    botStateCash.saveBotState(userId, BotState.START);
                }
            }
            case ADD_ONE_DAY -> {
                matcher = telegramTagPatternt.matcher(data);
                if (matcher.find()) {
                    user = userDAO.findByTelegramTag(data);
                    int amountOfDays = user.getAmountOfDays();
                    user.setAmountOfDays(amountOfDays + 1);
                    userDAO.save(user);
                    callbackAnswer = answerService.editKeyBoardWithMsg(userId, keyBoardTemplates.getSubscriptionKeyboard(true, userId, BotState.ADD_ONE_DAY, userDAO.findAllByPersonGroup(memory.getLastGroup())), messageId);
                } else {
                    callbackAnswer = answerService.sendText(userId, "Такой участник: " + data + " не найден в базе");
                    botStateCash.saveBotState(userId, BotState.START);
                }
            }
            case SET_GROUP -> {
                matcher = telegramTagPatternt.matcher(data);
                if (matcher.find()) {
                    user = userDAO.findByTelegramTag(data);
                    callbackAnswer = answerService.drawKeyBoardWithMsg(userId, keyBoardTemplates.getGroupsKeyboard(false, userId, BotState.SET_GROUP));
                } else {
                    callbackAnswer = answerService.sendText(userId, "Такой участник: " + data + " не найден в базе");
                    botStateCash.saveBotState(userId, BotState.START);
                }
            }
            case SET_DAYS -> {
                matcher = telegramTagPatternt.matcher(data);
                if (matcher.find()) {
                    user = userDAO.findByTelegramTag(data);
                    callbackAnswer = answerService.drawKeyBoardWithMsg(userId, keyBoardTemplates.getAmountOfDaysKeyboard(false, userId, BotState.START));
                } else {
                    callbackAnswer = answerService.sendText(userId, "Такой участник: " + data + " не найден в базе");
                    botStateCash.saveBotState(userId, BotState.START);
                }
            }
            case GET_GROUP_FOR_REMOVE_ONE_DAY -> {
                matcher = telegramTagPatternt.matcher(data);
                if (matcher.find()) {
                    user = userDAO.findByTelegramTag(data);
                    callbackAnswer = answerService.drawKeyBoardWithMsg(userId, keyBoardTemplates.getGroupsKeyboard(false, userId, BotState.GET_GROUP_FOR_REMOVE_ONE_DAY));
                } else {
                    callbackAnswer = answerService.sendText(userId, "Такой участник: " + data + " не найден в базе");
                    botStateCash.saveBotState(userId, BotState.GET_GROUP_FOR_REMOVE_ONE_DAY);
                }
            }
        }
        return callbackAnswer;
    }

    private BotApiMethod<?> promoteUser(long userId, int amountOfDays, Message message) {
        user.setAmountOfDays(user.getAmountOfDays() + amountOfDays);
        user.setSubscriber(true);
        userDAO.save(user);
        BotApiMethod<?> callBackAnswerInMethod;
        if (userDAO.findByTelegramTag(user.getTelegramTag()).getSubscriber()) {
            callBackAnswerInMethod = answerService.sendText(user.getTelegramId(),
                    "Ваша заявка подтверждена, вы оплатили " + amountOfDays + " тренировок");
            answerService.sendTextRightNow(userId, "Подписка для пользователя: " + user.getTelegramTag() + " успешно продлена на " + amountOfDays + " тренировок, теперь доступно: " + user.getAmountOfDays() + " тренировок.");
        } else {
            callBackAnswerInMethod = answerService.sendText(userId, "[ОШИБКА!] Подписка для пользователя: " + user.getTelegramTag() + " не продлена!");
        }
        return callBackAnswerInMethod;
    }

    private BotApiMethod<?> setGroup(long userId, Group group) {
        user.setPersonGroup(String.valueOf(group));
        user.setSubscriber(true);
        userDAO.save(user);
        BotApiMethod<?> callBackAnswerInMethod;
        if (userDAO.findByTelegramTag(user.getTelegramTag()).getPersonGroup().equals(String.valueOf(group))) {
            callBackAnswerInMethod = answerService.drawKeyBoardWithMsg(userId, keyBoardTemplates.getAmountOfDaysKeyboard(false, userId, BotState.SET_DAYS));
        } else {
            callBackAnswerInMethod = answerService.sendText(userId, "[ОШИБКА!] Подписка для пользователя: " + user.getTelegramTag() + " не продлена!");
        }
        return callBackAnswerInMethod;
    }

    private BotApiMethod<?> changeGroup(long userId, Group group) {
        user.setPersonGroup(String.valueOf(group));
        user.setSubscriber(true);
        userDAO.save(user);
        botStateCash.saveBotState(userId, BotState.SET_DAYS);
        return answerService.sendText(userId, "Группа для пользователя: " + user.getTelegramTag() + " успешно изменена");
    }

    private BotApiMethod<?> drawUsersFromGroup(long userId, BotState botState, Group group) {
        return answerService.drawKeyBoardWithMsg(userId, keyBoardTemplates.getSubscriptionKeyboard(false, userId, botState, userDAO.findAllByPersonGroup(String.valueOf(group))));
    }
}
