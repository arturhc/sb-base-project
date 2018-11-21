package com.company.appname.app.persistence.model;

import com.company.appname.common.persistence.model.BaseEntity;

import javax.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity(name = "User")
@Table(name = "User", schema = "appname")
public class User extends BaseEntity {

    @Column(length = 60, unique = true, nullable = false)
    private String email;

    @Column(length = 150, nullable = false)
    private String password;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 100, nullable = false)
    private String lastName;

    @Column(length = 15)
    private String phone;

    @Column(length = 10)
    private String phoneRegion;

    @Column
    private LocalDate birthday;

}
