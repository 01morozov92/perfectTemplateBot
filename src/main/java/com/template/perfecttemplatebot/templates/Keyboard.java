package com.template.perfecttemplatebot.templates;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Getter
@Setter
@Builder
public class Keyboard {

    private final InlineKeyboardMarkup replyKeyboard;
    private final String keyBoardDescription;

    public Keyboard(InlineKeyboardMarkup replyKeyboard, String keyBoardDescription) {
        this.replyKeyboard = replyKeyboard;
        this.keyBoardDescription = keyBoardDescription;
    }
}