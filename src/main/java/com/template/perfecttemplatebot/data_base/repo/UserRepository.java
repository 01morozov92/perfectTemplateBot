package com.template.perfecttemplatebot.data_base.repo;

import com.template.perfecttemplatebot.data_base.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findById(long id);
    User findByTelegramId(Long id);
    List<User> findAllBySubscriber(Boolean id);
}
