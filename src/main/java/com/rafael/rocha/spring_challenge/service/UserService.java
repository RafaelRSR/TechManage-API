package com.rafael.rocha.spring_challenge.service;

import com.rafael.rocha.spring_challenge.dto.UserResponseDTO;
import com.rafael.rocha.spring_challenge.exceptions.BusinessException;
import com.rafael.rocha.spring_challenge.exceptions.ResourceNotFoundException;
import com.rafael.rocha.spring_challenge.mapper.UserMapper;
import com.rafael.rocha.spring_challenge.model.entity.User;
import com.rafael.rocha.spring_challenge.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}