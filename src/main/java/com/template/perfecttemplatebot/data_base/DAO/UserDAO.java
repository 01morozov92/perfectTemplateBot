package com.template.perfecttemplatebot.data_base.DAO;

import com.template.perfecttemplatebot.cash.BotStateCash;
import com.template.perfecttemplatebot.data_base.entity.User;
import com.template.perfecttemplatebot.data_base.repo.UserRepository;
import com.template.perfecttemplatebot.enums.BotState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Service
public class UserDAO {
    private final BotStateCash botStateCash;
    private final UserRepository userRepository;

    @Autowired
    public UserDAO(BotStateCash botStateCash, UserRepository userRepository) {
        this.botStateCash = botStateCash;
        this.userRepository = userRepository;
    }

    public User findByUserId(long id) {
        return userRepository.findById(id);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void removeUser(User user) {
        userRepository.delete(user);
    }


    public void save(User user) {
        userRepository.save(user);
    }

    public SendMessage saveNewUser(Message message, long userId, SendMessage sendMessage) {
        String userName = message.getFrom().getUserName();
        User user = new User();
        user.setId(userId);
        user.setName("@" + userName);
        user.setAmountOfDays(0);
        this.save(user);
        sendMessage.setText("");
        botStateCash.saveBotState(userId, BotState.START);
        return sendMessage;
    }

    public boolean isExist(long id) {
        User user = findByUserId(id);
        return user != null;
    }
}
