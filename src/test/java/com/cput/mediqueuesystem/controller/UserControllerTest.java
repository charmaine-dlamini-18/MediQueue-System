package com.cput.mediqueuesystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.cput.mediqueuesystem.domain.Patient;
import com.cput.mediqueuesystem.domain.Role;
import com.cput.mediqueuesystem.domain.User;
import com.cput.mediqueuesystem.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private ObjectMapper objectMapper;
    private User user;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        Role role = new Role.Builder().setRoleId(1L).setRoleName("Patient").build();
        user = new Patient.Builder()
                .setUserId("U-001").setFirstName("Ellen").setLastName("Luella")
                .setEmail("ellen@example.com").setPassword("pass123")
                .setPhoneNumber("0821234567").setStatus(true)
                .setCreatedAt(LocalDateTime.now()).setRole(role)
                .setIdNumber("9001015800081").setDateOfBirth(java.time.LocalDate.of(1990, 1, 1))
                .setGender("Female").setAddress("123 Main Street")
                .build();
    }

    @Test
    void create_returnsCreatedUser() throws Exception {
        when(userService.create(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Ellen"));
    }

    @Test
    void read_withExistingId_returnsUser() throws Exception {
        when(userService.read("U-001")).thenReturn(user);

        mockMvc.perform(get("/user/read/U-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Ellen"));
    }

    @Test
    void update_returnsUpdatedUser() throws Exception {
        when(userService.update(any(User.class))).thenReturn(user);

        mockMvc.perform(put("/user/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Ellen"));
    }

    @Test
    void delete_returns200() throws Exception {
        mockMvc.perform(delete("/user/delete/U-001"))
                .andExpect(status().isOk());
    }

    @Test
    void getAll_returnsListOfUsers() throws Exception {
        when(userService.getAll()).thenReturn(List.of(user));

        mockMvc.perform(get("/user/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value("U-001"));
    }
}