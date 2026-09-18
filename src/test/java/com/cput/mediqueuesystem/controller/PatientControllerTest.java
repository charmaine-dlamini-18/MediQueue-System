package com.cput.mediqueuesystem.controller;

import java.time.LocalDateTime;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
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
import com.cput.mediqueuesystem.service.PatientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@WebMvcTest(PatientController.class)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    private ObjectMapper objectMapper;
    private Patient patient;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        Role role = new Role.Builder().setRoleId(1L).setRoleName("Patient").build();
        patient = new Patient.Builder().setCreatedAt(LocalDateTime.now())
                .setUserId("P-001").setFirstName("Ellen").setLastName("Luella")
                .setEmail("ellen@example.com").setPassword("pass123")
                .setPhoneNumber("0821234567").setStatus(true).setRole(role)
                .setIdNumber("9001015800081").setDateOfBirth(LocalDate.of(1990, 1, 1))
                .setGender("Female").setAddress("123 Main Street")
                .build();
    }

    @Test
    void create_returnsCreatedPatient() throws Exception {
        when(patientService.create(any(Patient.class))).thenReturn(patient);

        mockMvc.perform(post("/patient/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value("P-001"));
    }

    @Test
    void read_withExistingId_returnsPatient() throws Exception {
        when(patientService.read("P-001")).thenReturn(patient);

        mockMvc.perform(get("/patient/read/P-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Ellen"));
    }

    @Test
    void update_returnsUpdatedPatient() throws Exception {
        when(patientService.update(any(Patient.class))).thenReturn(patient);

        mockMvc.perform(put("/patient/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isOk());
    }

    @Test
    void delete_returns200() throws Exception {
        when(patientService.delete("P-001")).thenReturn(true);

        mockMvc.perform(delete("/patient/delete/P-001"))
                .andExpect(status().isOk());
    }

    @Test
    void getAll_returnsListOfPatients() throws Exception {
        when(patientService.getAll()).thenReturn(List.of(patient));

        mockMvc.perform(get("/patient/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value("P-001"));
    }
}