package com.rafael.rocha.spring_challenge.service;

import com.rafael.rocha.spring_challenge.dto.UserRequestDTO;
import com.rafael.rocha.spring_challenge.dto.UserResponseDTO;
import com.rafael.rocha.spring_challenge.exceptions.ResourceNotFoundException;
import com.rafael.rocha.spring_challenge.mapper.UserMapper;
import com.rafael.rocha.spring_challenge.model.entity.User;
import com.rafael.rocha.spring_challenge.model.enums.UserType;
import com.rafael.rocha.spring_challenge.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserRequestDTO userRequestDTO;
    private UserResponseDTO userResponseDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFullName("LeBron James");
        user.setEmail("lebron.james@nba.com");
        user.setPhone("+1 23 98765-4321");
        user.setBirthDate(new Date());
        user.setUserType(UserType.ADMIN);

        userRequestDTO = new UserRequestDTO();
        userRequestDTO.setFullName("LeBron James");
        userRequestDTO.setEmail("lebron.james@nba.com");
        userRequestDTO.setPhone("+1 23 98765-4321");
        userRequestDTO.setBirthDate(new Date());
        userRequestDTO.setUserType(UserType.ADMIN);

        userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(1L);
        userResponseDTO.setFullName("LeBron James");
        userResponseDTO.setEmail("lebron.james@nba.com");
        userResponseDTO.setPhone("+1 23 98765-4321");
        userResponseDTO.setBirthDate(new Date());
        userResponseDTO.setUserType(UserType.ADMIN);
    }

    @Test
    void findAllUsers_Success() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));
        when(userMapper.toDTO(any(User.class))).thenReturn(userResponseDTO);

        List<UserResponseDTO> result = userService.findAllUsers();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(userRepository).findAll();
    }

    @Test
    void getUserById_Success() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userMapper.toDTO(any(User.class))).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(userResponseDTO.getId(), result.getId());
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserById_NotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserById(1L));
    }

    @Test
    void createUser_Success() {
        when(userMapper.toEntity(any(UserRequestDTO.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDTO(any(User.class))).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.createUser(userRequestDTO);

        assertNotNull(result);
        assertEquals(userResponseDTO.getId(), result.getId());
        assertEquals(userResponseDTO.getEmail(), result.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUserById_Success() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDTO(any(User.class))).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.updateUserById(1L, userRequestDTO);

        assertNotNull(result);
        assertEquals(userResponseDTO.getId(), result.getId());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUserById_NotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.updateUserById(1L, userRequestDTO));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser_Success() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(any(User.class));

        assertDoesNotThrow(() -> userService.deleteUserById(1L));

        verify(userRepository).findById(1L);
        verify(userRepository).delete(user);
    }

    @Test
    void deleteUser_NotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.deleteUserById(1L));

        verify(userRepository, never()).delete(any(User.class));
    }
}