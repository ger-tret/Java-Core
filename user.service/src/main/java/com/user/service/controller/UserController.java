package com.user.service.controller;


import com.user.service.model.dto.UserDto;
import com.user.service.model.entity.User;
import com.user.service.service.UserService;
import com.user.service.validation.annotation.IdCsvValidation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    private UserService service;

    @Autowired
    UserController(UserService service){
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Long> createUserInstance(@RequestBody @Valid UserDto userDto){
        return ResponseEntity.ok((service.createUser(userDto)));
    }

    @PostMapping("/update")
    public ResponseEntity<Long> updateUserInstance(@RequestParam("id") Long id, @RequestBody @Valid UserDto userDto){
        return ResponseEntity.ok((service.updateUser(id, userDto)));
    }

    @GetMapping("/find")
    public ResponseEntity<UserDto> findUser(@RequestParam("id") Long id){
        return ResponseEntity.ok(service.findUserById(id));
    }

    @DeleteMapping
    public ResponseEntity<Long> deleteUser(@RequestParam("id") Long id){
        return ResponseEntity.ok(service.deleteUser(id));
    }

    @GetMapping("/findbycsv")
    public ResponseEntity<List<User>> findUsersByIdCsv(@RequestParam("id") @IdCsvValidation String id){
        return ResponseEntity.ok(((service.findUsersByIdCsv(id))));
    }



}
