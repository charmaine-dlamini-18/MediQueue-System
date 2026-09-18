package com.cput.mediqueuesystem.service;


import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cput.mediqueuesystem.domain.Patient;
import com.cput.mediqueuesystem.domain.Role;
import com.cput.mediqueuesystem.repository.PatientRepository;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    private Patient patient;

    @BeforeEach
    void setUp() {
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
    void create_savesAndReturnsPatient() {
        // NOTE: PatientService does not call PatientFactory before saving.
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        Patient result = patientService.create(patient);

        assertEquals("P-001", result.getUserId());
        verify(patientRepository, times(1)).save(patient);
    }

    @Test
    void read_withExistingId_returnsPatient() {
        when(patientRepository.findById("P-001")).thenReturn(Optional.of(patient));

        Patient result = patientService.read("P-001");

        assertEquals("Ellen", result.getFirstName());
    }

    @Test
    void read_withNonExistingId_returnsNull() {
        when(patientRepository.findById("P-999")).thenReturn(Optional.empty());

        Patient result = patientService.read("P-999");

        assertNull(result);
    }

    @Test
    void update_savesAndReturnsPatient() {
        when(patientRepository.save(patient)).thenReturn(patient);

        Patient result = patientService.update(patient);

        assertEquals("P-001", result.getUserId());
    }

    @Test
    void delete_callsRepositoryDeleteById_andReturnsTrue() {
        boolean result = patientService.delete("P-001");

        assertEquals(true, result);
        verify(patientRepository, times(1)).deleteById("P-001");
    }

    @Test
    void getAll_returnsAllPatients() {
        when(patientRepository.findAll()).thenReturn(List.of(patient));

        List<Patient> result = patientService.getAll();

        assertEquals(1, result.size());
    }
}