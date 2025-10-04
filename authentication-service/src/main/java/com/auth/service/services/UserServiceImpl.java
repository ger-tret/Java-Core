package com.auth.service.services;

import com.auth.service.model.User;
import com.auth.service.model.dto.IdDto;
import com.auth.service.model.dto.request.RegisterRequestDto;
import com.auth.service.repository.UserRepository;
import com.auth.service.services.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    public Long registerUser(RegisterRequestDto registerRequestDto) {
        return (userRepository.save(userMapper.toEntity(registerRequestDto)).getId());
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        final User user = userRepository.findByLogin(login);

        if (user == null) {
            throw new UsernameNotFoundException("User '" + login + "' not found");
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(login)
                .password(user.getPassword())
                .authorities(user.getUserRoles())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
    }
