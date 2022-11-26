package com.template.perfecttemplatebot.handlers;

import com.template.perfecttemplatebot.bot.TelegramBot;
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

import static com.template.perfecttemplatebot.app_config.ApplicationContextProvider.getApplicationContext;

@Component
//processes incoming callback's
public class CallbackQueryHandler {
    private final BotStateCash botStateCash;
    private final KeyBoardTemplates keyBoardTemplates;
    private final AnswerService answerService;
    private final UserDAO userDAO;

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

        BotApiMethod<?> callBackAnswer = null;

        String data = buttonQuery.getData();

        switch (data) {
            case ("back_from_waiting_list"):
                callBackAnswer = keyBoardTemplates.getMainMenuMessage(chatId,
                        "Воспользуйтесь главным меню", userId);
                botStateCash.saveBotState(userId, BotState.START);
                break;
            //первая клавиатура
            case ("first_button"):
                callBackAnswer = answerService.drawKeyBoardWithMsg(userId, keyBoardTemplates.getSecondKeyBoard());
                botStateCash.saveBotState(userId, BotState.START);
                break;
            case ("second_button"):
                callBackAnswer = new SendMessage(String.valueOf(chatId), "Вторая заглушка");
                botStateCash.saveBotState(userId, BotState.START);
                break;
            case ("third_button"):
                callBackAnswer = new SendMessage(String.valueOf(chatId), "Тертья заглушка");
                botStateCash.saveBotState(userId, BotState.START);
                break;
            case ("fourth_button"):
                callBackAnswer = new SendMessage(String.valueOf(chatId), "Четвертая заглушка");
                botStateCash.saveBotState(userId, BotState.START);
                break;
            //вторая клавиатура
            case ("first_btn_second_menu"):
                callBackAnswer = answerService.drawKeyBoardWithMsg(chatId, keyBoardTemplates.getThirdKeyBoard());
                botStateCash.saveBotState(userId, BotState.START);
                break;
            case ("second_btn_second_menu"):
                callBackAnswer = new SendMessage(String.valueOf(chatId), "Вторая заглушка подменю 1");
                botStateCash.saveBotState(userId, BotState.START);
                break;
            case ("third_btn_second_menu"):
                callBackAnswer = new SendMessage(String.valueOf(chatId), "Третья заглушка подменю 1");
                botStateCash.saveBotState(userId, BotState.START);
                break;
            //третья клавиатура
            case ("first_btn_third_menu"):
                callBackAnswer = new SendMessage(String.valueOf(chatId), "Первая заглушка подменю 2");
                botStateCash.saveBotState(userId, BotState.SUB_MENU_2);
                break;
            case ("second_btn_third_menu"):
                callBackAnswer = new SendMessage(String.valueOf(chatId), "Вторая заглушка подменю 2");
                botStateCash.saveBotState(userId, BotState.START);
                break;
            case ("third_btn_third_menu"):
                callBackAnswer = new SendMessage(String.valueOf(chatId), "Третья заглушка подменю 2");
                botStateCash.saveBotState(userId, BotState.START);
                break;
            default:
                Pattern authPattern = Pattern.compile("^@[_a-zA-Z]+$");
                Matcher matcher = authPattern.matcher(data);
                if (matcher.find()) {
                    User user = userDAO.findByTelegramTag(data);
                    user.setSubscriber(true);
                    userDAO.save(user);
                    callBackAnswer = answerService.sendText(user.getTelegramId(), "Ваша заявка подтверждена, теперь вы можете пользоваться ботом");
                    if (userDAO.findByTelegramTag(data).getSubscriber()) {
                        answerService.sendTextRightNow(userId, "Подписка для пользователя: " + user.getTelegramTag() + " успешно продлена");
                    } else {
                        answerService.sendTextRightNow(userId, "[ОШИБКА!] Подписка для пользователя: " + user.getTelegramTag() + " не продлена!");
                    }
                    botStateCash.saveBotState(userId, BotState.START);
                } else {
                    callBackAnswer = answerService.sendText(userId, "Такой участник: " + data + " не найден в базе");
                }
        }
        return callBackAnswer;
    }
}
