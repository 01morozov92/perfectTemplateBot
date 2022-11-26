package com.template.perfecttemplatebot.templates;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Getter
@Setter
@Builder
public class KeyBoard {

    private final ReplyKeyboard replyKeyboard;
    private final String keyBoardDescription;

    public KeyBoard(ReplyKeyboard replyKeyboard, String keyBoardDescription) {
        this.replyKeyboard = replyKeyboard;
        this.keyBoardDescription = keyBoardDescription;
    }
}