package com.auth.service.model;

import com.auth.service.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String login;

    private String password;

    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<UserRole> userRoles;


}
