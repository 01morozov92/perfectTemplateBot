package com.template.perfecttemplatebot.bot;

import com.template.perfecttemplatebot.cash.BotStateCash;
import com.template.perfecttemplatebot.enums.BotState;
import com.template.perfecttemplatebot.handlers.CallbackQueryHandler;
import com.template.perfecttemplatebot.handlers.MessageHandler;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramFacade {

    final MessageHandler messageHandler;
    final CallbackQueryHandler callbackQueryHandler;
    final BotStateCash botStateCash;

    @Value("${telegrambot.adminId}")
    int adminId;


    public TelegramFacade(MessageHandler messageHandler, CallbackQueryHandler callbackQueryHandler, BotStateCash botStateCash) {
        this.messageHandler = messageHandler;
        this.callbackQueryHandler = callbackQueryHandler;
        this.botStateCash = botStateCash;
    }

    //Срабатывает после любого апдейта, проверяет колбэк или месадж
    public BotApiMethod<?> handleUpdate(Update update) {

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            return callbackQueryHandler.processCallbackQuery(callbackQuery);
        } else {

            Message message = update.getMessage();
            if (message != null && message.hasText()) {
                return handleInputMessage(message);
            }
        }
        return null;
    }

    //обработка текстового сообщения и установка соответсвующего состояния бота
    private BotApiMethod<?> handleInputMessage(Message message) {
        BotState botState;
        String inputMsg = message.getText();
        //we process messages of the main menu and any other messages
        //set state
        switch (inputMsg) {
            case "/start":
                botState = BotState.START;
                break;
            case "Меню1":
                botState = BotState.MENU_1;
                break;
            case "Меню2":
                botState = BotState.MENU_2;
                break;
            case "Меню3":
                botState = BotState.MENU_3;
                break;
            case "Админсоке меню 1":
                if (message.getFrom().getId() == adminId)
                botState = BotState.ADMIN_MENU_1;
                else botState = BotState.START;
                break;
            case "Админсоке меню 2":
                if (message.getFrom().getId() == adminId)
                botState = BotState.ADMIN_MENU_2;
                else botState = BotState.START;
                break;
            default:
                botState = botStateCash.getBotStateMap().get(message.getFrom().getId()) == null?
                        BotState.START: botStateCash.getBotStateMap().get(message.getFrom().getId());
        }
        //we pass the corresponding state to the handler
        //the corresponding method will be called
        return messageHandler.handle(message, botState);

    }
}
