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
import com.cput.mediqueuesystem.domain.Visit;
import com.cput.mediqueuesystem.service.IVisitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@WebMvcTest(VisitController.class)
class VisitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IVisitService visitService;

    private ObjectMapper objectMapper;
    private Visit visit;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        Patient patient = new Patient.Builder().setCreatedAt(LocalDateTime.now()).setUserId("P-001").build();
        visit = new Visit.Builder()
                .setVisitId("V-0001")
                .setPatient(patient)
                .setVisitDate(LocalDate.of(2026, 5, 7))
                .setStatus("Pending")
                .build();
    }

    @Test
    void create_withValidVisit_returns201() throws Exception {
        when(visitService.create(any(Visit.class))).thenReturn(visit);

        mockMvc.perform(post("/mediqueue/visit/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(visit)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.visitId").value("V-0001"));
    }

    @Test
    void create_withInvalidVisit_returns400() throws Exception {
        when(visitService.create(any(Visit.class))).thenReturn(null);

        mockMvc.perform(post("/mediqueue/visit/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(visit)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void read_withExistingId_returns200() throws Exception {
        when(visitService.read("V-0001")).thenReturn(visit);

        mockMvc.perform(get("/mediqueue/visit/read/V-0001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Pending"));
    }

    @Test
    void read_withNonExistingId_returns404() throws Exception {
        when(visitService.read("V-9999")).thenReturn(null);

        mockMvc.perform(get("/mediqueue/visit/read/V-9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_withExistingVisit_returns200() throws Exception {
        when(visitService.update(any(Visit.class))).thenReturn(visit);

        mockMvc.perform(put("/mediqueue/visit/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(visit)))
                .andExpect(status().isOk());
    }

    @Test
    void update_withNonExistingVisit_returns404() throws Exception {
        when(visitService.update(any(Visit.class))).thenReturn(null);

        mockMvc.perform(put("/mediqueue/visit/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(visit)))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_returns204() throws Exception {
        mockMvc.perform(delete("/mediqueue/visit/delete/V-0001"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAll_returnsListOfVisits() throws Exception {
        when(visitService.getAll()).thenReturn(List.of(visit));

        mockMvc.perform(get("/mediqueue/visit/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].visitId").value("V-0001"));
    }

    @Test
    void getAll_whenEmpty_returnsEmptyArray() throws Exception {
        when(visitService.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/mediqueue/visit/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }
}