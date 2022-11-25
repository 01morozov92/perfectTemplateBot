package com.template.perfecttemplatebot.data_base.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    public User() {
    }
}
