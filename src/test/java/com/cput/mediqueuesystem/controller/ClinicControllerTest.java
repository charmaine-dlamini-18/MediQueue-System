package com.cput.mediqueuesystem.controller;

import static org.mockito.ArgumentMatchers.any;
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

import com.cput.mediqueuesystem.domain.Clinic;
import com.cput.mediqueuesystem.service.IClinicService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ClinicController.class)
class ClinicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IClinicService clinicService;

    private ObjectMapper objectMapper;
    private Clinic clinic;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        clinic = new Clinic.Builder().setClinicId("C-001").setClinicName("District Six Clinic").build();
    }

    @Test
    void create_withValidClinic_returns201() throws Exception {
        when(clinicService.create(any(Clinic.class))).thenReturn(clinic);

        mockMvc.perform(post("/clinic/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clinic)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clinicId").value("C-001"));
    }

    @Test
    void read_withExistingId_returnsClinic() throws Exception {
        when(clinicService.read("C-001")).thenReturn(clinic);

        mockMvc.perform(get("/clinic/read/C-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clinicName").value("District Six Clinic"));
    }

    @Test
    void update_returnsUpdatedClinic() throws Exception {
        when(clinicService.update(any(Clinic.class))).thenReturn(clinic);

        mockMvc.perform(put("/clinic/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clinic)))
                .andExpect(status().isOk());
    }

    @Test
    void delete_returnsBooleanTrue() throws Exception {
        when(clinicService.delete("C-001")).thenReturn(true);

        mockMvc.perform(delete("/clinic/delete/C-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    void getAll_returnsListOfClinics() throws Exception {
        when(clinicService.getAll()).thenReturn(List.of(clinic));

        mockMvc.perform(get("/clinic/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clinicId").value("C-001"));
    }
}