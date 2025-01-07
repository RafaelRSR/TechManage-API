package com.rafael.rocha.spring_challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafael.rocha.spring_challenge.dto.UserRequestDTO;
import com.rafael.rocha.spring_challenge.dto.UserResponseDTO;
import com.rafael.rocha.spring_challenge.exceptions.ResourceNotFoundException;
import com.rafael.rocha.spring_challenge.model.enums.UserType;
import com.rafael.rocha.spring_challenge.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserRequestDTO userRequestDTO;
    private UserResponseDTO userResponseDTO;

    @BeforeEach
    void setUp() {
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
    void createUser_Success() throws Exception {
        when(userService.createUser(any(UserRequestDTO.class))).thenReturn(userResponseDTO);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void createUser_InvalidData() throws Exception {
        userRequestDTO.setEmail("invalid-email");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllUsers_Success() throws Exception {
        when(userService.findAllUsers()).thenReturn(Arrays.asList(userResponseDTO));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk());
    }

    @Test
    void getUserById_Success() throws Exception {
        when(userService.getUserById(1L)).thenReturn(userResponseDTO);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getUserById_NotFound() throws Exception {
        when(userService.getUserById(anyLong())).thenThrow(new ResourceNotFoundException("User not found"));

        mockMvc.perform(get("/api/users/1"))  // Corrigido o path
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUser_Success() throws Exception {
        when(userService.updateUserById(anyLong(), any(UserRequestDTO.class))).thenReturn(userResponseDTO);

        mockMvc.perform(put("/api/users/1")  // Corrigido o path
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser_Success() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser_NotFound() throws Exception {
        doThrow(new ResourceNotFoundException("User not found")).when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNotFound());
    }
}