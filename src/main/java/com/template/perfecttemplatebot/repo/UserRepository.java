package com.template.perfecttemplatebot.repo;

import com.template.perfecttemplatebot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findById(long id);
}
