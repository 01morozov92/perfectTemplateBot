package com.template.perfecttemplatebot.handlers;

import com.template.perfecttemplatebot.service.AnswerService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramFacade {

    final MessageHandler messageHandler;
    private boolean firstTimeIncome = true;
    public static Integer mainMessageId;
    final CallbackQueryHandler callbackQueryHandler;
    private final AnswerService answerService;

    public TelegramFacade(MessageHandler messageHandler, CallbackQueryHandler callbackQueryHandler, AnswerService answerService) {
        this.messageHandler = messageHandler;
        this.callbackQueryHandler = callbackQueryHandler;
        this.answerService = answerService;
    }

    //Срабатывает после любого апдейта, проверяет колбэк или месадж
    public BotApiMethod<?> handleUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            return callbackQueryHandler.processCallbackQuery(callbackQuery);
        } else {
            if (firstTimeIncome){
                Message message = update.getMessage();
                mainMessageId = message.getMessageId();
                firstTimeIncome = false;
            }
            Message message = update.getMessage();
            if (message != null && message.hasText()) {
//                answerService.deleteAllMessages(message.getChatId(), message);
                return messageHandler.handleInputMessage(message);
            }
        }
        return null;
    }
}
