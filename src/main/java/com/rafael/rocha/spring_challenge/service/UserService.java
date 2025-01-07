package com.rafael.rocha.spring_challenge.service;

import com.rafael.rocha.spring_challenge.dto.UserRequestDTO;
import com.rafael.rocha.spring_challenge.dto.UserResponseDTO;
import com.rafael.rocha.spring_challenge.exceptions.BusinessException;
import com.rafael.rocha.spring_challenge.exceptions.ResourceNotFoundException;
import com.rafael.rocha.spring_challenge.mapper.UserMapper;
import com.rafael.rocha.spring_challenge.model.entity.User;
import com.rafael.rocha.spring_challenge.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public List<UserResponseDTO> findAllUsers() {
        try {
            log.info("getAllUsers() - INIT - fetching all users");

            List<User> users = userRepository.findAll();
            List<UserResponseDTO> userDTOs = users.stream()
                    .map(userMapper::toDTO)
                    .collect(Collectors.toList());

            log.info("getAllUsers() - END - retrieved total users[{}]", userDTOs.size());
            return userDTOs;

        } catch (Exception e) {
            log.error("getAllUsers() - ERROR - failed to fetch users", e);
            throw new BusinessException("Error retrieving users", e);
        }
    }

    public UserResponseDTO getUserById(Long id) {
        try {
            log.info("getUserById() - INIT - fetching user with id[{}]", id);

            User user = userRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("getUserById() - ERROR - user not found with id[{}]", id);
                        return new ResourceNotFoundException("User not found with id: " + id);
                    });

            UserResponseDTO responseDTO = userMapper.toDTO(user);
            log.info("getUserById() - END - successfully retrieved user with id[{}]", id);
            return responseDTO;

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("getUserById() - ERROR - failed to fetch user with id[{}]", id, e);
            throw new BusinessException("Error retrieving user", e);
        }
    }

    @Transactional
    public UserResponseDTO createUser(@Valid UserRequestDTO userRequestDTO) {
        try {
            log.info("createUser() - INIT - creating user with email[{}]", userRequestDTO.getEmail());

            User user = userMapper.toEntity(userRequestDTO);
            User savedUser = userRepository.save(user);
            UserResponseDTO responseDTO = userMapper.toDTO(savedUser);

            log.info("createUser() - END - successfully created user with id[{}], email[{}]",
                    savedUser.getId(), savedUser.getEmail());
            return responseDTO;

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("createUser() - ERROR - failed to create user with email[{}]",
                    userRequestDTO.getEmail(), e);
            throw new BusinessException("Error creating user", e);
        }
    }

    @Transactional
    public UserResponseDTO updateUserById(Long id, @Valid UserRequestDTO userRequestDTO) {
        try {
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

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("updateUser() - ERROR - failed to update user with id[{}]", id, e);
            throw new BusinessException("Error updating user", e);
        }
    }
}