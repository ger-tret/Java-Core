package com.user.service;

import com.user.service.model.dto.UserDto;
import com.user.service.model.entity.User;
import com.user.service.repository.UserRepository;
import com.user.service.service.UserServiceImpl;
import com.user.service.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class UserTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private UserDto testUserDto;

    @BeforeEach
    void setUp() {
        testUser = new User(1L, "John", "Doe", new Date(1990, 1, 1), "john@example.com", new ArrayList<>());
        testUserDto = new UserDto(1L, "John", "Doe", new Date(1990, 1, 1), "john@example.com", new ArrayList<>());
    }

    @Test
    void createUser_ShouldReturnUserId_WhenValidInput() {

        when(userMapper.toEntity(testUserDto)).thenReturn(testUser);
        when(userRepository.save(testUser)).thenReturn(testUser);


        Long result = userService.createUser(testUserDto);


        assertEquals(1L, result);
        verify(userRepository).save(testUser);
    }

    @Test
    void findUserById_ShouldReturnUserDto_WhenUserExists() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userMapper.toDto(testUser)).thenReturn(testUserDto);


        UserDto result = userService.findUserById(1L);


        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        verify(userRepository).findById(1L);
    }

    @Test
    void findUserById_ShouldThrowException_WhenUserNotFound() {

        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.findUserById(999L));
    }

    @Test
    void findUserByEmail_ShouldReturnUser_WhenEmailExists() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(testUser));

        Optional<User> result = userService.findUserByEmail("john@example.com");

        assertTrue(result.isPresent());
        assertEquals("john@example.com", result.get().getEmail());
    }

    @Test
    void findUserByEmail_ShouldReturnEmpty_WhenEmailNotFound() {

        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());


        Optional<User> result = userService.findUserByEmail("unknown@example.com");


        assertTrue(result.isEmpty());
    }

    @Test
    void findUsersByIdCsv_ShouldReturnUsers_WhenIdsExist() {

        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        User user2 = new User(2L, "Jane", "Smith", new Date(1985, 5, 15), "jane@example.com", new ArrayList<>());
        User user3 = new User(3L, "Bob", "Wilson", new Date(1992, 8, 20), "bob@example.com", new ArrayList<>());

        when(userRepository.findAllById(ids)).thenReturn(Arrays.asList(testUser, user2, user3));


        List<User> result = userService.findUsersByIdCsv("1,2,3");


        assertEquals(3, result.size());
        verify(userRepository).findAllById(ids);
    }

    @Test
    void findUsersByIdCsv_ShouldReturnEmptyList_WhenNoIdsFound() {

        when(userRepository.findAllById(anyList())).thenReturn(Collections.emptyList());


        List<User> result = userService.findUsersByIdCsv("1,2,3");


        assertTrue(result.isEmpty());
    }

    @Test
    void updateUser_ShouldUpdateAndReturnId_WhenUserExists() {

        UserDto updatedDto = new UserDto(1L, "Johnny", "Doeman", new Date(1991, 2, 2), "johnny@example.com", new ArrayList<>());

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(testUser)).thenReturn(testUser);


        Long result = userService.updateUser(1L, updatedDto);


        assertEquals(1L, result);
        assertEquals("Johnny", testUser.getName());
        assertEquals("Doeman", testUser.getSurname());
        verify(userRepository).save(testUser);
    }

    @Test
    void updateUser_ShouldThrowException_WhenUserNotFound() {

        UserDto updatedDto = new UserDto(999L, "Johnny", "Doeman", new Date(1991, 2, 2), "johnny@example.com", new ArrayList<>());
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.updateUser(999L, updatedDto));
    }

    @Test
    void deleteUser_ShouldReturnId_WhenUserDeleted() {

        doNothing().when(userRepository).deleteById(1L);


        Long result = userService.deleteUser(1L);


        assertEquals(1L, result);
        verify(userRepository).deleteById(1L);
    }
}