package com.cput.mediqueuesystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.cput.mediqueuesystem.domain.Role;
import com.cput.mediqueuesystem.service.RoleService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(RoleController.class)
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoleService roleService;

    private ObjectMapper objectMapper;
    private Role role;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        role = new Role.Builder().setRoleId(1L).setRoleName("Nurse").build();
    }

    @Test
    void getAllRoles_returnsListOfRoles() throws Exception {
        when(roleService.getAllRoles()).thenReturn(List.of(role));

        mockMvc.perform(get("/api/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].roleName").value("Nurse"));
    }

    @Test
    void getRoleById_withExistingId_returns200() throws Exception {
        when(roleService.getRoleById(1L)).thenReturn(role);

        mockMvc.perform(get("/api/roles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roleName").value("Nurse"));
    }

    @Test
    void getRoleById_withNonExistingId_returns404() throws Exception {
        when(roleService.getRoleById(999L)).thenReturn(null);

        mockMvc.perform(get("/api/roles/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createRole_returns201() throws Exception {
        when(roleService.createRole(any(Role.class))).thenReturn(role);

        mockMvc.perform(post("/api/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(role)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.roleName").value("Nurse"));
    }

    @Test
    void updateRole_withExistingId_returns200() throws Exception {
        when(roleService.updateRole(anyLong(), any(Role.class))).thenReturn(role);

        mockMvc.perform(put("/api/roles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(role)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roleName").value("Nurse"));
    }

    @Test
    void updateRole_withNonExistingId_returns404() throws Exception {
        when(roleService.updateRole(anyLong(), any(Role.class))).thenReturn(null);

        mockMvc.perform(put("/api/roles/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(role)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteRole_whenExists_returns204() throws Exception {
        when(roleService.roleExists(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/roles/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteRole_whenNotExists_returns404() throws Exception {
        when(roleService.roleExists(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/roles/999"))
                .andExpect(status().isNotFound());
    }
}