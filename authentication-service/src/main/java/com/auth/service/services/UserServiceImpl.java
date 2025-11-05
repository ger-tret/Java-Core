package com.auth.service.services;

import com.auth.service.model.dto.request.LoginRequestDto;
import com.auth.service.model.dto.request.RegisterRequestDto;
import com.auth.service.model.enums.UserRole;
import com.auth.service.repository.UserRepository;
import com.auth.service.services.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import com.auth.service.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.security.core.userdetails.User.withUsername;


import static com.auth.service.model.User.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    @Override
    public Long registerUser(RegisterRequestDto registerRequestDto) {
        String hashedPassword = passwordEncoder.encode(registerRequestDto.getPassword());
        registerRequestDto.setPassword(hashedPassword);
        return (userRepository.save(userMapper.toEntity(registerRequestDto)).getId());
    }

    @Override
    public UserDetails loadUserByUsername(String login) {
        User user = userRepository.findByLogin(login);
        return buildUserDetails(user);
    }

    private UserDetails buildUserDetails(User user) {
        List<GrantedAuthority> authorities = UserRole.toAuthorities(user.getUserRoles());

        return  withUsername(user.getLogin())
                .password(user.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }


    @Override
    public List<UserRole> getUserRolesByLogin(LoginRequestDto loginRequestDto) {
       return userRepository.findByLogin(loginRequestDto.getLogin()).getUserRoles();
    }
}
