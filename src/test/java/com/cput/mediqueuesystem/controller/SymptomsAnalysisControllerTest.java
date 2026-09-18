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
import com.cput.mediqueuesystem.domain.SymptomsAnalysis;
import com.cput.mediqueuesystem.service.ISymptomsAnalysisService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@WebMvcTest(SymptomsAnalysisController.class)
class SymptomsAnalysisControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ISymptomsAnalysisService symptomsAnalysisService;

    private ObjectMapper objectMapper;
    private SymptomsAnalysis analysis;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        Patient patient = new Patient.Builder().setUserId("P-001").build();
        analysis = new SymptomsAnalysis.Builder()
                .setAnalysisId("SA-001").setPatient(patient)
                .setInputText("Headache").setPredictedConditions("Flu")
                .setConfidenceScore(0.85).setCreatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void create_withValidAnalysis_returns201() throws Exception {
        when(symptomsAnalysisService.create(any(SymptomsAnalysis.class))).thenReturn(analysis);

        mockMvc.perform(post("/mediqueue/symptoms-analysis/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(analysis)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.analysisId").value("SA-001"));
    }

    @Test
    void create_withInvalidAnalysis_returns400() throws Exception {
        when(symptomsAnalysisService.create(any(SymptomsAnalysis.class))).thenReturn(null);

        mockMvc.perform(post("/mediqueue/symptoms-analysis/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(analysis)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void read_withExistingId_returns200() throws Exception {
        when(symptomsAnalysisService.read("SA-001")).thenReturn(analysis);

        mockMvc.perform(get("/mediqueue/symptoms-analysis/read/SA-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.predictedConditions").value("Flu"));
    }

    @Test
    void read_withNonExistingId_returns404() throws Exception {
        when(symptomsAnalysisService.read("SA-999")).thenReturn(null);

        mockMvc.perform(get("/mediqueue/symptoms-analysis/read/SA-999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_withExistingAnalysis_returns200() throws Exception {
        when(symptomsAnalysisService.update(any(SymptomsAnalysis.class))).thenReturn(analysis);

        mockMvc.perform(put("/mediqueue/symptoms-analysis/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(analysis)))
                .andExpect(status().isOk());
    }

    @Test
    void update_withNonExistingAnalysis_returns404() throws Exception {
        when(symptomsAnalysisService.update(any(SymptomsAnalysis.class))).thenReturn(null);

        mockMvc.perform(put("/mediqueue/symptoms-analysis/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(analysis)))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_returns204() throws Exception {
        mockMvc.perform(delete("/mediqueue/symptoms-analysis/delete/SA-001"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAll_returnsListOfAnalyses() throws Exception {
        when(symptomsAnalysisService.getAll()).thenReturn(List.of(analysis));

        mockMvc.perform(get("/mediqueue/symptoms-analysis/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].analysisId").value("SA-001"));
    }
}