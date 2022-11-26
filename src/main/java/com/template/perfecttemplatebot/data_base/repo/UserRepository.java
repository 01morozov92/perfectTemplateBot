package com.template.perfecttemplatebot.data_base.repo;

import com.template.perfecttemplatebot.data_base.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findById(long id);
    User findByTelegramId(Long id);
}
