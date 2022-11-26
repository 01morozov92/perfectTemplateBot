package com.template.perfecttemplatebot.data_base.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "telegram_tag")
    private String telegramTag;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "amount_of_days")
    private Integer amountOfDays;

    public User() {
    }
}
