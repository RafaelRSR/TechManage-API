package com.rafael.rocha.spring_challenge.controller;

import com.rafael.rocha.spring_challenge.dto.UserRequestDTO;
import com.rafael.rocha.spring_challenge.dto.UserResponseDTO;
import com.rafael.rocha.spring_challenge.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userRequestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUserById(@PathVariable Long id,
                                                      @Valid @RequestBody UserRequestDTO userUpdateDTO) {
        return ResponseEntity.ok(userService.updateUserById(id, userUpdateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
