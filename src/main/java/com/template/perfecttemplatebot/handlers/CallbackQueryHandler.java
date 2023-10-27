package com.template.perfecttemplatebot.handlers;

import com.template.perfecttemplatebot.cash.BotStateCash;
import com.template.perfecttemplatebot.cash.Memory;
import com.template.perfecttemplatebot.data_base.DAO.UserDAO;
import com.template.perfecttemplatebot.enums.BotState;
import com.template.perfecttemplatebot.service.AnswerService;
import com.template.perfecttemplatebot.templates.KeyBoardTemplates;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.regex.Pattern;

@Component
//processes incoming callback's
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
            case ("back") -> callbackAnswer = answerService.drawKeyBoardWithMsg(userId, memory.getPreviewKeyBoard(userId));
            //первая клавиатура
            case ("first_button") ->
                    callbackAnswer = answerService.drawKeyBoardWithMsg(userId, keyBoardTemplates.getSecondKeyBoard(
                            false, userId, BotState.SUB_MENU_1));
            case ("second_button") -> {
                callbackAnswer = new SendMessage(String.valueOf(chatId), "Вторая заглушка");
                botStateCash.saveBotState(userId, BotState.START);
            }
            case ("third_button") -> {
                callbackAnswer = new SendMessage(String.valueOf(chatId), "Тертья заглушка");
                botStateCash.saveBotState(userId, BotState.START);
            }
            case ("fourth_button") -> {
                callbackAnswer = answerService.sendText(chatId, "Четвертая заглушка");
                botStateCash.saveBotState(userId, BotState.START);
            }
            //вторая клавиатура
            case ("first_btn_second_menu") -> callbackAnswer = answerService.drawKeyBoardWithMsg(
                    userId, keyBoardTemplates.getThirdKeyBoard(false, userId, BotState.SUB_MENU_2));
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
        }
        return callbackAnswer;
    }
}
