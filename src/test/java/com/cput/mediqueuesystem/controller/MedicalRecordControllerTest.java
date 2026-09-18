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

import com.cput.mediqueuesystem.domain.MedicalRecord;
import com.cput.mediqueuesystem.domain.Patient;
import com.cput.mediqueuesystem.domain.Staff;
import com.cput.mediqueuesystem.service.IMedicalRecordService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@WebMvcTest(MedicalRecordController.class)
class MedicalRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IMedicalRecordService medicalRecordService;

    private ObjectMapper objectMapper;
    private MedicalRecord record;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        Patient patient = new Patient.Builder().setCreatedAt(LocalDateTime.now()).setUserId("P-001").build();
        Staff staff = new Staff.Builder().setUserId("U-001").build();

        record = new MedicalRecord.Builder()
                .setRecordId("MR-001").setPatient(patient).setCreatedBy(staff)
                .setDiagnosis("Flu").setRecordDate(LocalDate.of(2026, 5, 7))
                .build();
    }

    @Test
    void create_returnsCreatedRecord() throws Exception {
        when(medicalRecordService.create(any(MedicalRecord.class))).thenReturn(record);

        mockMvc.perform(post("/medical-record/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(record)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recordId").value("MR-001"));
    }

    @Test
    void read_withExistingId_returnsRecord() throws Exception {
        when(medicalRecordService.read("MR-001")).thenReturn(record);

        mockMvc.perform(get("/medical-record/read/MR-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.diagnosis").value("Flu"));
    }

    @Test
    void update_returnsUpdatedRecord() throws Exception {
        when(medicalRecordService.update(any(MedicalRecord.class))).thenReturn(record);

        mockMvc.perform(put("/medical-record/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(record)))
                .andExpect(status().isOk());
    }

    @Test
    void delete_returnsBooleanTrue() throws Exception {
        when(medicalRecordService.delete("MR-001")).thenReturn(true);

        mockMvc.perform(delete("/medical-record/delete/MR-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    void getAll_returnsListOfRecords() throws Exception {
        when(medicalRecordService.getAll()).thenReturn(List.of(record));

        mockMvc.perform(get("/medical-record/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].recordId").value("MR-001"));
    }
}