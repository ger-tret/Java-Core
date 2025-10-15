package com.user.service.service;

import com.user.service.model.dto.UserDto;
import com.user.service.model.entity.User;
import com.user.service.repository.UserRepository;
import com.user.service.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImpl implements UserService {

    private UserRepository repository;
    private UserMapper mapper;


    @Transactional
    @Override
    @CachePut(value = "customers", key = "#customer.id")
    public Long createUser(UserDto userDto) {
        return repository.save(mapper.toEntity(userDto)).getUserId();
    }

    @Override
    @Cacheable(value = "customers", key = "#id")
    public UserDto findUserById(Long id) {
        return mapper.toDto(repository.findById(id).orElseThrow(() -> new NoSuchElementException("User for ID= " + id + "not found")));
    }

    @Override
    @Cacheable(value = "customers")
    public Optional<User> findUserByEmail(String email) {
        return repository.findByEmail(email);
    }


    @Override
    @Cacheable(value = "customers")
    public List<User> findUsersByIdCsv(String idCsv) {
        List<Long> ids = Arrays.stream(idCsv.split(",")).map(Long::parseLong).toList();
        List<User> foundUsers = repository.findAllById(ids).stream().toList();
        return foundUsers;
    }

    @Transactional
    @Override
    @CachePut(value = "customers", key = "#customer.id")
    public Long updateUser(Long id, UserDto updatedUser){
        return repository.findById(id).map(user ->{
            user.setName(updatedUser.getName());
            user.setSurname(updatedUser.getSurname());
            user.setEmail(updatedUser.getEmail());
            user.setBirthDate(updatedUser.getBirthDate());
            return repository.save(user);
        }).orElseThrow(() -> new NoSuchElementException("User for ID= " + id + "not found")).getUserId();
    }

    @Transactional
    @Override
    @CacheEvict(value = "customers", key = "#id")
    public Long deleteUser(Long id) {
        repository.deleteById(id);
        return id;
    }
}
