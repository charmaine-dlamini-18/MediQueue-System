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

import com.cput.mediqueuesystem.domain.Clinic;
import com.cput.mediqueuesystem.domain.Patient;
import com.cput.mediqueuesystem.domain.Queue;
import com.cput.mediqueuesystem.domain.QueueEntry;
import com.cput.mediqueuesystem.domain.Staff;
import com.cput.mediqueuesystem.service.IQueueEntryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@WebMvcTest(QueueEntryController.class)
class QueueEntryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IQueueEntryService queueEntryService;

    private ObjectMapper objectMapper;
    private QueueEntry entry;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Clinic clinic = new Clinic.Builder().setClinicId("C-001").build();
        Queue queue = new Queue.Builder().setQueueId("Q-001").setClinic(clinic).setDate(LocalDate.now()).build();
        Patient patient = new Patient.Builder().setCreatedAt(LocalDateTime.now()).setUserId("P-001").build();
        Staff doctor = new Staff.Builder().setUserId("U-001").build();

        entry = new QueueEntry.Builder()
                .setQueueEntryId("QE-001").setQueue(queue).setPatient(patient).setDoctor(doctor)
                .setQueueNumber(1).setStatus("Waiting")
                .build();
    }

    @Test
    void create_withValidEntry_returns201() throws Exception {
        when(queueEntryService.create(any(QueueEntry.class))).thenReturn(entry);

        mockMvc.perform(post("/mediqueue/queue-entry/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entry)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.queueEntryId").value("QE-001"));
    }

    @Test
    void create_withInvalidEntry_returns400() throws Exception {
        when(queueEntryService.create(any(QueueEntry.class))).thenReturn(null);

        mockMvc.perform(post("/mediqueue/queue-entry/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entry)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void read_withExistingId_returns200() throws Exception {
        when(queueEntryService.read("QE-001")).thenReturn(entry);

        mockMvc.perform(get("/mediqueue/queue-entry/read/QE-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Waiting"));
    }

    @Test
    void read_withNonExistingId_returns404() throws Exception {
        when(queueEntryService.read("QE-999")).thenReturn(null);

        mockMvc.perform(get("/mediqueue/queue-entry/read/QE-999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_withExistingEntry_returns200() throws Exception {
        when(queueEntryService.update(any(QueueEntry.class))).thenReturn(entry);

        mockMvc.perform(put("/mediqueue/queue-entry/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entry)))
                .andExpect(status().isOk());
    }

    @Test
    void update_withNonExistingEntry_returns404() throws Exception {
        when(queueEntryService.update(any(QueueEntry.class))).thenReturn(null);

        mockMvc.perform(put("/mediqueue/queue-entry/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entry)))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_returns204() throws Exception {
        mockMvc.perform(delete("/mediqueue/queue-entry/delete/QE-001"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAll_returnsListOfEntries() throws Exception {
        when(queueEntryService.getAll()).thenReturn(List.of(entry));

        mockMvc.perform(get("/mediqueue/queue-entry/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].queueEntryId").value("QE-001"));
    }
}