package com.auth.service.services.mapper;

import com.auth.service.model.User;
import com.auth.service.model.dto.request.RegisterRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    RegisterRequestDto toDto(User user);
    User toEntity(RegisterRequestDto userDto);
}