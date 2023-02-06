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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "telegram_id")
    private Long telegramId;

    @Column(name = "telegram_tag")
    private String telegramTag;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "amount_of_days")
    private Integer amountOfDays;

    @Column(name = "subscriber")
    private Boolean subscriber;

    @Column(name = "person_group")
    private String personGroup;

    public User() {
    }
}
