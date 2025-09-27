package com.user.service.service;

import com.user.service.model.dto.UserDto;
import com.user.service.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Long createUser(UserDto userDto);
    UserDto findUserById(Long id);
    Optional<User> findUserByEmail(String email);
    List<User> findUsersByIdCsv(String idCsv);
    Long updateUser(Long id, UserDto updatedUser);
    Long deleteUser(Long id);
}
