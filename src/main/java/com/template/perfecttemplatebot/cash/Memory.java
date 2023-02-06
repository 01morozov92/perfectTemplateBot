package com.template.perfecttemplatebot.cash;

import com.template.perfecttemplatebot.templates.Keyboard;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Getter
@Setter
public class Memory {

    private Integer mainMenuMessageId;
    private Integer mainMessageId;
    private String lastGroup;
    private final BotStateCash botStateCash;
    private List<Keyboard> keyboards = new ArrayList<>();

    public Memory(BotStateCash botStateCash) {
        this.botStateCash = botStateCash;
    }

    public Keyboard getPreviewKeyBoard(Long userId) {
        Keyboard lastKeyboard = keyboards.get(keyboards.size() - 1);
        botStateCash.saveBotState(userId, keyboards.get(keyboards.indexOf(lastKeyboard) - 1).getBotState());
        keyboards.add(keyboards.get(keyboards.indexOf(lastKeyboard) - 1));
        return lastKeyboard.getPreviewKeyBoard();
    }

    public void saveKeyboard(Keyboard keyboard) {
        keyboards.add(keyboard);
    }
}
