package com.template.perfecttemplatebot.templates;

import com.template.perfecttemplatebot.cash.BotStateCash;
import com.template.perfecttemplatebot.cash.Memory;
import com.template.perfecttemplatebot.enums.BotState;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Getter
@Setter
public class Keyboard {

    private final ReplyKeyboard replyKeyboard;
    private final String keyBoardDescription;
    private final Memory memory;
    private final BotState botState;
    private final BotStateCash botStateCash;
    private final Long userId;
    private Keyboard previewKeyBoard;

    public Keyboard(InlineKeyboardMarkup replyKeyboard, String keyBoardDescription, Memory memory, BotState botState, BotStateCash botStateCash, Long userId) {
        this.replyKeyboard = replyKeyboard;
        this.keyBoardDescription = keyBoardDescription;
        this.memory = memory;
        this.botState = botState;
        this.botStateCash = botStateCash;
        this.userId = userId;
        memory.saveKeyboard(this);
        botStateCash.saveBotState(userId, botState);
    }

    public Keyboard(InlineKeyboardMarkup replyKeyboard, String keyBoardDescription, Memory memory, BotState botState, BotStateCash botStateCash, Long userId, Keyboard previewKeyBoard) {
        this.replyKeyboard = replyKeyboard;
        this.keyBoardDescription = keyBoardDescription;
        this.memory = memory;
        this.botState = botState;
        this.botStateCash = botStateCash;
        this.userId = userId;
        this.previewKeyBoard = previewKeyBoard;
        memory.saveKeyboard(this);
        botStateCash.saveBotState(userId, botState);
    }

    public Keyboard(String message, InlineKeyboardMarkup replyKeyboard, String keyBoardDescription, Memory memory, BotState botState, BotStateCash botStateCash, Long userId) {
        this.replyKeyboard = replyKeyboard;
        this.keyBoardDescription = keyBoardDescription;
        this.memory = memory;
        this.botState = botState;
        this.botStateCash = botStateCash;
        this.userId = userId;
        botStateCash.saveBotState(userId, botState);
    }
}