package com.user.service.service.mapper;

import com.user.service.model.dto.UserDto;
import com.user.service.model.entity.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(UserDto userDto);
}