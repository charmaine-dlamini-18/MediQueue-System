package com.cput.mediqueuesystem.controller;

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
import com.cput.mediqueuesystem.domain.Prescription;
import com.cput.mediqueuesystem.service.IPrescriptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@WebMvcTest(PrescriptionController.class)
class PrescriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IPrescriptionService prescriptionService;

    private ObjectMapper objectMapper;
    private Prescription prescription;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        MedicalRecord record = new MedicalRecord.Builder().setRecordId("MR-001").build();
        prescription = new Prescription.Builder()
                .setPrescriptionId(String.valueOf(1L)).setMedicalRecord(record)
                .setMedicationName("Paracetamol").setDosage("500mg")
                .setPrescriptionDate(LocalDate.of(2026, 5, 7))
                .build();
    }

    @Test
    void create_returnsCreatedPrescription() throws Exception {
        when(prescriptionService.create(any(Prescription.class))).thenReturn(prescription);

        mockMvc.perform(post("/prescription/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(prescription)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.prescriptionId").value(1));
    }

    @Test
    void read_withExistingId_returnsPrescription() throws Exception {
        when(prescriptionService.read("RX-001")).thenReturn(prescription);

        mockMvc.perform(get("/prescription/read/RX-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.medicationName").value("Paracetamol"));
    }

    @Test
    void update_returnsUpdatedPrescription() throws Exception {
        when(prescriptionService.update(any(Prescription.class))).thenReturn(prescription);

        mockMvc.perform(put("/prescription/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(prescription)))
                .andExpect(status().isOk());
    }

    @Test
    void delete_returnsBooleanTrue() throws Exception {
        when(prescriptionService.delete("RX-001")).thenReturn(true);

        mockMvc.perform(delete("/prescription/delete/RX-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    void getAll_returnsListOfPrescriptions() throws Exception {
        when(prescriptionService.getAll()).thenReturn(List.of(prescription));

        mockMvc.perform(get("/prescription/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].prescriptionId").value(1));
    }
}