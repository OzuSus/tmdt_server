package com.TTLTTBDD.server.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user", nullable = false)
    private Integer id;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @NotNull
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "fullname", nullable = false, length = 50)
    private String fullname;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Column(name = "address", nullable = false, length = 100)
    private String address;

    @Column(name = "role", nullable = false)
    private Boolean role = false;

    @Column(name = "avatar", nullable = true)
    private String avatar;

    @Column(name = "status", nullable = false)
    private Boolean status = false;


}