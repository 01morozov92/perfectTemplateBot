package com.template.perfecttemplatebot.cash;

import com.template.perfecttemplatebot.enums.BotState;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Setter
@Getter
//Used to save state bot.
public class BotStateCash {

    private BotState lastBotState;
    private final Map<Long, BotState> botStateMap = new HashMap<>();

    public BotState saveBotState(long userId, BotState botState) {
        botStateMap.put(userId, botState);
        lastBotState = botState;
        return botStateMap.get(userId);
    }
}
