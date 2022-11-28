package com.template.perfecttemplatebot.handlers;

import com.template.perfecttemplatebot.cash.BotStateCash;
import com.template.perfecttemplatebot.data_base.DAO.UserDAO;
import com.template.perfecttemplatebot.data_base.entity.User;
import com.template.perfecttemplatebot.enums.BotState;
import com.template.perfecttemplatebot.service.AnswerService;
import com.template.perfecttemplatebot.templates.KeyBoardTemplates;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CallbackQueryHandler {
    private final BotStateCash botStateCash;
    private final KeyBoardTemplates keyBoardTemplates;
    private final AnswerService answerService;
    private final UserDAO userDAO;
    private final Pattern telegramTagPatternt = Pattern.compile("^@[_a-zA-Z]+$");
    private BotApiMethod<?> callbackAnswer;
    private User user;

    @Autowired
    public CallbackQueryHandler(BotStateCash botStateCash, KeyBoardTemplates keyBoardTemplates, AnswerService answerService, UserDAO userDAO) {
        this.botStateCash = botStateCash;
        this.keyBoardTemplates = keyBoardTemplates;
        this.answerService = answerService;
        this.userDAO = userDAO;
    }

    @SneakyThrows
    public BotApiMethod<?> processCallbackQuery(CallbackQuery callbackQuery) {
        final long chatId = callbackQuery.getMessage().getChatId();
        final long userId = callbackQuery.getFrom().getId();
        callbackQuery.getMessage().getMessageId();


        String data = callbackQuery.getData();
        switch (data) {
            case ("back_from_waiting_list") -> {
                callbackAnswer = answerService.sendText(userId,
                        "Воспользуйтесь главным меню");
                botStateCash.saveBotState(userId, BotState.START);
            }
            //выбор количества дней
            case ("8"), ("16"), ("24"), ("32") -> {
                callbackAnswer = promoteUser(userId, callbackAnswer, Integer.parseInt(data));
                botStateCash.saveBotState(userId, BotState.START);
            }
            //вторая клавиатура
            case ("first_btn_second_menu") -> {
                callbackAnswer = answerService.drawKeyBoardWithMsg(chatId, keyBoardTemplates.getThirdKeyBoard());
                botStateCash.saveBotState(userId, BotState.START);
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
            default -> callbackAnswer = handleDynamicCallbacks(userId, data);
        }
        return callbackAnswer;
    }

    private BotApiMethod<?> handleDynamicCallbacks(long userId, String data) {
        switch (botStateCash.getLastBotState()) {
            case ADD_ONE_DAY:
                Matcher matcher = telegramTagPatternt.matcher(data);
                if (matcher.find()) {
                    user = userDAO.findByTelegramTag(data);
                    botStateCash.saveBotState(userId, BotState.ADD_ONE_DAY);
                    int amountOfDays = user.getAmountOfDays();
                    user.setAmountOfDays(amountOfDays + 1);
                    userDAO.save(user);
                    callbackAnswer = answerService.drawKeyBoardWithMsg(userId, keyBoardTemplates.getSubscriptionKeyboard());
                } else {
                    callbackAnswer = answerService.sendText(userId, "Такой участник: " + data + " не найден в базе");
                    botStateCash.saveBotState(userId, BotState.START);
                }
                break;
            case REMOVE_ONE_DAY:
                matcher = telegramTagPatternt.matcher(data);
                if (matcher.find()) {
                    user = userDAO.findByTelegramTag(data);
                    botStateCash.saveBotState(userId, BotState.REMOVE_ONE_DAY);
                    int amountOfDays = user.getAmountOfDays();
                    if (amountOfDays > 0) {
                        user.setAmountOfDays(amountOfDays - 1);
                        userDAO.save(user);
                    } else {
                        answerService.sendTextRightNow(userId, "У участника: " + data + " уже закончились оплаченные тренировки");
                    }
                    callbackAnswer = answerService.drawKeyBoardWithMsg(userId, keyBoardTemplates.getSubscriptionKeyboard());
                } else {
                    callbackAnswer = answerService.sendText(userId, "Такой участник: " + data + " не найден в базе");
                    botStateCash.saveBotState(userId, BotState.START);
                }
                break;
            case CHECK_WAITING_ROOM:
            case RENEW_SUBSCRIPTION:
                matcher = telegramTagPatternt.matcher(data);
                if (matcher.find()) {
                    user = userDAO.findByTelegramTag(data);
                    botStateCash.saveBotState(userId, BotState.SET_DAYS);
                    callbackAnswer = answerService.drawKeyBoardWithMsg(userId, keyBoardTemplates.getAmountOfDaysKeyBoard());
                } else {
                    callbackAnswer = answerService.sendText(userId, "Такой участник: " + data + " не найден в базе");
                    botStateCash.saveBotState(userId, BotState.START);
                }
                break;
        }
        return callbackAnswer;
    }

    private BotApiMethod<?> promoteUser(long userId, BotApiMethod<?> callBackAnswer, int amountOfDays) {
        user.setAmountOfDays(user.getAmountOfDays() + amountOfDays);
        user.setSubscriber(true);
        userDAO.save(user);
        if (userDAO.findByTelegramTag(user.getTelegramTag()).getSubscriber()) {
            callBackAnswer = answerService.sendText(user.getTelegramId(),
                    "Ваша заявка подтверждена, вы оплатили " + amountOfDays + " тренировок");
            answerService.sendTextRightNow(userId, "Подписка для пользователя: " + user.getTelegramTag() + " успешно продлена на " + amountOfDays + " тренировок, теперь доступно: " + user.getAmountOfDays() + " тренировок.");
        } else {
            answerService.sendTextRightNow(userId, "[ОШИБКА!] Подписка для пользователя: " + user.getTelegramTag() + " не продлена!");
        }
        return callBackAnswer;
    }
}
