package com.template.perfecttemplatebot.data_base.DAO;

import com.template.perfecttemplatebot.data_base.entity.User;
import com.template.perfecttemplatebot.data_base.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Comparator;
import java.util.List;

@Service
public class UserDAO {

    private final Comparator<User> comparator = Comparator.comparingInt(User::getId);
    private final UserRepository userRepository;

    @Autowired
    public UserDAO( UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUserId(int id) {
        return userRepository.findById(id);
    }

    public User findByTelegramId(long id) {
        return userRepository.findByTelegramId(id);
    }
    public User findByTelegramTag(String telegramTag) {
        return userRepository.findByTelegramTag(telegramTag);
    }
    public List<User> findAllBySubscriber(Boolean subscription) {
        List<User> users = userRepository.findAllBySubscriber(subscription);
        users.sort(comparator);
        return users;
    }

    public List<User> findAllUsers() {
        List<User> users = userRepository.findAll();
        users.sort(comparator);
        return users;
    }

    public void removeUser(User user) {
        userRepository.delete(user);
    }


    public void save(User user) {
        userRepository.save(user);
    }

    public SendMessage saveUser(Message message, long userId, SendMessage sendMessage, String lastName, String firstName, Boolean subscriber) {
        String userName = message.getFrom().getUserName();
        User user = findByTelegramId(userId);
        user.setSubscriber(subscriber);
        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setTelegramId(userId);
        user.setTelegramTag("@" + userName);
        user.setAmountOfDays(0);
        this.save(user);
        sendMessage.setText("");
        return sendMessage;
    }

    public SendMessage addNewUser(Message message, long userId, SendMessage sendMessage) {
        User user = new User();
        user.setTelegramId(userId);
        user.setTelegramTag("@" + message.getFrom().getUserName());
        user.setSubscriber(false);
        sendMessage.setText("");
        this.save(user);
        return sendMessage;
    }

    public boolean isExist(long id) {
        User user = findByTelegramId(id);
        return user != null;
    }

    public boolean isSubscriber(long id) {
        User user = findByTelegramId(id);
        return user.getSubscriber().equals(true);
    }

    public boolean hasName(long id) {
        User user = findByTelegramId(id);
        return user.getFirstName() != null & user.getFirstName() != null;
    }
}
