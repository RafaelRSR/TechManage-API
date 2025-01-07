package com.rafael.rocha.spring_challenge.service;

import com.rafael.rocha.spring_challenge.dto.UserRequestDTO;
import com.rafael.rocha.spring_challenge.dto.UserResponseDTO;
import com.rafael.rocha.spring_challenge.exceptions.ResourceNotFoundException;
import com.rafael.rocha.spring_challenge.mapper.UserMapper;
import com.rafael.rocha.spring_challenge.model.entity.User;
import com.rafael.rocha.spring_challenge.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAllUsers() {
        log.info("getAllUsers() - INIT - fetching all users");

        List<User> users = userRepository.findAll();
        List<UserResponseDTO> userDTOs = users.stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());

        log.info("getAllUsers() - END - retrieved total users[{}]", userDTOs.size());
        return userDTOs;
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        log.info("getUserById() - INIT - fetching user with id[{}]", id);

        UserResponseDTO responseDTO = userRepository.findById(id)
                .map(userMapper::toDTO)
                .orElseThrow(() -> {
                    log.error("getUserById() - ERROR - user not found with id[{}]", id);
                    return new ResourceNotFoundException("User not found with id: " + id);
                });

        log.info("getUserById() - END - successfully retrieved user with id[{}]", id);
        return responseDTO;
    }

    @Transactional
    public UserResponseDTO createUser(@Valid UserRequestDTO userRequestDTO) {
        log.info("createUser() - INIT - creating user with email[{}]", userRequestDTO.getEmail());

        User user = userMapper.toEntity(userRequestDTO);
        User savedUser = userRepository.save(user);
        UserResponseDTO responseDTO = userMapper.toDTO(savedUser);

        log.info("createUser() - END - successfully created user with id[{}], email[{}]",
                savedUser.getId(), savedUser.getEmail());
        return responseDTO;
    }

    @Transactional
    public UserResponseDTO updateUserById(Long id, @Valid UserRequestDTO userRequestDTO) {
        log.info("updateUser() - INIT - updating user with id[{}]", id);

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("updateUser() - ERROR - user not found with id[{}]", id);
                    return new ResourceNotFoundException("User not found with id: " + id);
                });

        userMapper.updateUserFromDTO(userRequestDTO, existingUser);
        User updatedUser = userRepository.save(existingUser);
        UserResponseDTO responseDTO = userMapper.toDTO(updatedUser);

        log.info("updateUser() - END - successfully updated user with id[{}]", id);
        return responseDTO;
    }

    @Transactional
    public void deleteUserById(Long id) {
        log.info("Attempting to delete user with id: {}", id);

        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with id: {}", id);
                    return new ResourceNotFoundException("User not found with id: " + id);
                });

        userRepository.delete(userToDelete);
        log.info("Successfully deleted user with id: {}", id);
    }
}