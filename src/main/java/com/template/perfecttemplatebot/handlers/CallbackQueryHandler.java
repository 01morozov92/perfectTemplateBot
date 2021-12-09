package com.template.perfecttemplatebot.handlers;

import com.template.perfecttemplatebot.cash.BotStateCash;
import com.template.perfecttemplatebot.enums.BotState;
import com.template.perfecttemplatebot.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

@Component
//processes incoming callback's
public class CallbackQueryHandler {
    private final BotStateCash botStateCash;
    private final MenuService menuService;
    private final SimpleHandler simpleHandler;

    @Autowired
    public CallbackQueryHandler(BotStateCash botStateCash, MenuService menuService, SimpleHandler simpleHandler) {
        this.botStateCash = botStateCash;
        this.menuService = menuService;
        this.simpleHandler = simpleHandler;
    }

    public BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) {
        final long chatId = buttonQuery.getMessage().getChatId();
        final long userId = buttonQuery.getFrom().getId();

        BotApiMethod<?> callBackAnswer = null;

        String data = buttonQuery.getData();

        switch (data) {
            //первая клавиатура
            case ("first_button"):
                callBackAnswer = simpleHandler.drawSecondKeyBoardWithMsg(userId);
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
                callBackAnswer = simpleHandler.drawThirdKeyBoardWithMsg(chatId);
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
        }
        return callBackAnswer;
    }
}
