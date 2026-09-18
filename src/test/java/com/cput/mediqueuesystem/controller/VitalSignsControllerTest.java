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
import com.cput.mediqueuesystem.domain.VitalSigns;
import com.cput.mediqueuesystem.domain.Visit;
import com.cput.mediqueuesystem.service.IVitalSignsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@WebMvcTest(VitalSignsController.class)
class VitalSignsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IVitalSignsService vitalSignsService;

    private ObjectMapper objectMapper;
    private VitalSigns vitals;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        Patient patient = new Patient.Builder().setCreatedAt(LocalDateTime.now()).setUserId("P-001").build();
        Visit visit = new Visit.Builder()
                .setVisitId("V-001")
                .setPatient(patient)
                .setVisitDate(LocalDate.of(2026, 5, 7))
                .setStatus("Pending")
                .build();

        vitals = new VitalSigns.Builder()
                .setVitalId("VS-0001")
                .setVisit(visit)
                .setTemperature("36.8")
                .setBloodPressure("117/76")
                .setHeartRate("78")
                .setWeight("80")
                .build();
    }

    @Test
    void create_withValidVitalSigns_returns201() throws Exception {
        when(vitalSignsService.create(any(VitalSigns.class))).thenReturn(vitals);

        mockMvc.perform(post("/mediqueue/vital-signs/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vitals)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.vitalId").value("VS-0001"));
    }

    @Test
    void create_withInvalidVitalSigns_returns400() throws Exception {
        when(vitalSignsService.create(any(VitalSigns.class))).thenReturn(null);

        mockMvc.perform(post("/mediqueue/vital-signs/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vitals)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void read_withExistingId_returns200() throws Exception {
        when(vitalSignsService.read("VS-0001")).thenReturn(vitals);

        mockMvc.perform(get("/mediqueue/vital-signs/read/VS-0001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.temperature").value("36.8"));
    }

    @Test
    void read_withNonExistingId_returns404() throws Exception {
        when(vitalSignsService.read("VS-9999")).thenReturn(null);

        mockMvc.perform(get("/mediqueue/vital-signs/read/VS-9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_withExistingVitalSigns_returns200() throws Exception {
        when(vitalSignsService.update(any(VitalSigns.class))).thenReturn(vitals);

        mockMvc.perform(put("/mediqueue/vital-signs/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vitals)))
                .andExpect(status().isOk());
    }

    @Test
    void update_withNonExistingVitalSigns_returns404() throws Exception {
        when(vitalSignsService.update(any(VitalSigns.class))).thenReturn(null);

        mockMvc.perform(put("/mediqueue/vital-signs/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vitals)))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_returns204() throws Exception {
        mockMvc.perform(delete("/mediqueue/vital-signs/delete/VS-0001"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAll_returnsListOfVitalSigns() throws Exception {
        when(vitalSignsService.getAll()).thenReturn(List.of(vitals));

        mockMvc.perform(get("/mediqueue/vital-signs/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].vitalId").value("VS-0001"));
    }

    @Test
    void getAll_whenEmpty_returnsEmptyArray() throws Exception {
        when(vitalSignsService.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/mediqueue/vital-signs/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}