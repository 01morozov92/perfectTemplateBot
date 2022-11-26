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
//processes incoming callback's
public class CallbackQueryHandler {
    private final BotStateCash botStateCash;
    private final KeyBoardTemplates keyBoardTemplates;
    private final AnswerService answerService;
    private final UserDAO userDAO;

    private User user;

    @Autowired
    public CallbackQueryHandler(BotStateCash botStateCash, KeyBoardTemplates keyBoardTemplates, AnswerService answerService, UserDAO userDAO) {
        this.botStateCash = botStateCash;
        this.keyBoardTemplates = keyBoardTemplates;
        this.answerService = answerService;
        this.userDAO = userDAO;
    }

    @SneakyThrows
    public BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) {
        final long chatId = buttonQuery.getMessage().getChatId();
        final long userId = buttonQuery.getFrom().getId();

        BotApiMethod<?> callbackAnswer = null;

        String data = buttonQuery.getData();

        switch (data) {
            case ("back_from_waiting_list"):
                callbackAnswer = keyBoardTemplates.getMainMenuMessage(
                        "Воспользуйтесь главным меню", userId);
                botStateCash.saveBotState(userId, BotState.START);
                break;
            //выбор количества дней
            case ("8"):
                user.setAmountOfDays(user.getAmountOfDays() + 8);
                callbackAnswer = promoteUser(userId, callbackAnswer);
                botStateCash.saveBotState(userId, BotState.START);
                break;
            case ("16"):
                user.setAmountOfDays(user.getAmountOfDays() + 16);
                callbackAnswer = promoteUser(userId, callbackAnswer);
                botStateCash.saveBotState(userId, BotState.START);
                break;
            case ("24"):
                user.setAmountOfDays(user.getAmountOfDays() + 24);
                callbackAnswer = promoteUser(userId, callbackAnswer);
                botStateCash.saveBotState(userId, BotState.START);
                break;
            case ("32"):
                user.setAmountOfDays(user.getAmountOfDays() + 32);
                callbackAnswer = promoteUser(userId, callbackAnswer);
                botStateCash.saveBotState(userId, BotState.START);
                break;
            //вторая клавиатура
            case ("first_btn_second_menu"):
                callbackAnswer = answerService.drawKeyBoardWithMsg(chatId, keyBoardTemplates.getThirdKeyBoard());
                botStateCash.saveBotState(userId, BotState.START);
                break;
            case ("second_btn_second_menu"):
                callbackAnswer = new SendMessage(String.valueOf(chatId), "Вторая заглушка подменю 1");
                botStateCash.saveBotState(userId, BotState.START);
                break;
            case ("third_btn_second_menu"):
                callbackAnswer = new SendMessage(String.valueOf(chatId), "Третья заглушка подменю 1");
                botStateCash.saveBotState(userId, BotState.START);
                break;
            //третья клавиатура
            case ("first_btn_third_menu"):
                callbackAnswer = new SendMessage(String.valueOf(chatId), "Первая заглушка подменю 2");
                botStateCash.saveBotState(userId, BotState.SUB_MENU_2);
                break;
            case ("second_btn_third_menu"):
                callbackAnswer = new SendMessage(String.valueOf(chatId), "Вторая заглушка подменю 2");
                botStateCash.saveBotState(userId, BotState.START);
                break;
            case ("third_btn_third_menu"):
                callbackAnswer = new SendMessage(String.valueOf(chatId), "Третья заглушка подменю 2");
                botStateCash.saveBotState(userId, BotState.START);
                break;
            default:
                Pattern authPattern = Pattern.compile("^@[_a-zA-Z]+$");
                Matcher matcher = authPattern.matcher(data);
                if (matcher.find()) {
                    user = userDAO.findByTelegramTag(data);
                    botStateCash.saveBotState(userId, BotState.SET_DAYS);
                    callbackAnswer = answerService.drawKeyBoardWithMsg(userId, keyBoardTemplates.getAmountOfDaysKeyBoard());
                } else {
                    callbackAnswer = answerService.sendText(userId, "Такой участник: " + data + " не найден в базе");
                    botStateCash.saveBotState(userId, BotState.START);
                }
        }
        return callbackAnswer;
    }

    private BotApiMethod<?> promoteUser(long userId, BotApiMethod<?> callBackAnswer) {
        user.setSubscriber(true);
        userDAO.save(user);
        if (userDAO.findByTelegramTag(user.getTelegramTag()).getSubscriber()) {
            callBackAnswer = keyBoardTemplates.getMainMenuMessage(
                    "Ваша заявка подтверждена, вы оплатили " + user.getAmountOfDays() + " тренировок и теперь вы можете пользоваться ботом ", user.getTelegramId());
            answerService.sendTextRightNow(userId, "Подписка для пользователя: " + user.getTelegramTag() + " успешно продлена на " + user.getAmountOfDays() + " тренировок");
        } else {
            answerService.sendTextRightNow(userId, "[ОШИБКА!] Подписка для пользователя: " + user.getTelegramTag() + " не продлена!");
        }
        return callBackAnswer;
    }
}
