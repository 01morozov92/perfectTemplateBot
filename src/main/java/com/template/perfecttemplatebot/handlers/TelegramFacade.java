package com.template.perfecttemplatebot.handlers;

import com.template.perfecttemplatebot.cash.BotStateCash;
import com.template.perfecttemplatebot.enums.BotState;
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

    //обработка текстового сообщения и установка соответствующего состояния бота
    private BotApiMethod<?> handleInputMessage(Message message) {
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
        return messageHandler.handle(message, botState);

    }
}
