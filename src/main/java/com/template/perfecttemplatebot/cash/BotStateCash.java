package com.template.perfecttemplatebot.cash;

import com.template.perfecttemplatebot.enums.BotState;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Setter
@Getter
//Used to save state bot.
public class BotStateCash {

    private BotState lastBotState;
    private final Map<Long, BotState> botStateMap = new LinkedHashMap<>();

    public void saveBotState(long userId, BotState botState) {
        botStateMap.put(userId, botState);
        lastBotState = botState;
        botStateMap.get(userId);
    }

    public BotState getPreviewBotState() {
        ArrayList<BotState> botStates = new ArrayList<>(botStateMap.values());
        return botStates.get(botStates.size() - 2);
    }
}
