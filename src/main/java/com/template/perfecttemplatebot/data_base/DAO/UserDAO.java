package com.template.perfecttemplatebot.data_base.DAO;

import com.template.perfecttemplatebot.data_base.entity.User;
import com.template.perfecttemplatebot.data_base.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Comparator;
import java.util.List;

@Service
public class UserDAO {

    private final Comparator<User> comparator = Comparator.comparingInt(User::getId);
    private final UserRepository userRepository;

    @Autowired
    public UserDAO(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUserId(int id) {
        return userRepository.findById(id);
    }

    public User findByTelegramId(long id) {
        return userRepository.findByTelegramId(id);
    }

    public User findBySubscriber(boolean subscriber) {
        return userRepository.findBySubscriber(subscriber);
    }

    public User findByTelegramTag(String telegramTag) {
        return userRepository.findByTelegramTag(telegramTag);
    }

    public List<User> findAllBySubscriber(Boolean subscription) {
        List<User> users = userRepository.findAllBySubscriber(subscription);
        users.sort(comparator);
        return users;
    }

    public List<User> findAllByPersonGroup(String group) {
        List<User> users = userRepository.findAllByPersonGroup(group);
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

    public void saveUser(Message message, long userId, String lastName, String firstName, Boolean subscriber) {
        User user = findByTelegramId(userId);
        user.setSubscriber(subscriber);
        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setTelegramId(userId);
        user.setTelegramTag("@" + message.getFrom().getUserName());
        user.setAmountOfDays(0);
        this.save(user);
    }

    public void addNewUser(Message message, long userId) {
        User user = new User();
        user.setTelegramId(userId);
        user.setTelegramTag("@" + message.getFrom().getUserName());
        user.setSubscriber(false);
        this.save(user);
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
