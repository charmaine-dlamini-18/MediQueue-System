package com.cput.mediqueuesystem.repository;

import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.cput.mediqueuesystem.domain.Patient;
import com.cput.mediqueuesystem.domain.Role;
import com.cput.mediqueuesystem.domain.SymptomsAnalysis;

@DataJpaTest
class SymptomsAnalysisRepositoryTest {

    @Autowired
    private SymptomsAnalysisRepository symptomsAnalysisRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private RoleRepository roleRepository;

    private Patient patient;

    @BeforeEach
    void setUp() {
        Role role = roleRepository.save(new Role.Builder().setRoleName("Patient").build());
        patient = patientRepository.save(new Patient.Builder().setCreatedAt(LocalDateTime.now())
                .setUserId("P-001").setFirstName("Ellen").setLastName("Luella")
                .setEmail("ellen@example.com").setPassword("pass123").setPhoneNumber("0821234567")
                .setStatus(true).setRole(role)
                .setIdNumber("9001015800081").setDateOfBirth(LocalDate.of(1990, 1, 1))
                .setGender("Female").setAddress("123 Main Street").build());
    }

    @Test
    void save_persistsSymptomsAnalysis() {
        SymptomsAnalysis analysis = new SymptomsAnalysis.Builder()
                .setAnalysisId("SA-001").setPatient(patient).setInputText("Headache").build();

        SymptomsAnalysis saved = symptomsAnalysisRepository.save(analysis);

        assertEquals("SA-001", saved.getAnalysisId());
    }

    @Test
    void findById_whenAnalysisExists_returnsAnalysis() {
        symptomsAnalysisRepository.save(new SymptomsAnalysis.Builder()
                .setAnalysisId("SA-002").setPatient(patient).setInputText("Fever").build());

        Optional<SymptomsAnalysis> found = symptomsAnalysisRepository.findById("SA-002");

        assertTrue(found.isPresent());
        assertEquals("Fever", found.get().getInputText());
    }

    @Test
    void findById_whenAnalysisDoesNotExist_returnsEmpty() {
        assertFalse(symptomsAnalysisRepository.findById("SA-999").isPresent());
    }

    @Test
    void existsById_afterSave_returnsTrue() {
        symptomsAnalysisRepository.save(new SymptomsAnalysis.Builder()
                .setAnalysisId("SA-003").setPatient(patient).setInputText("Cough").build());

        assertTrue(symptomsAnalysisRepository.existsById("SA-003"));
    }

    @Test
    void deleteById_removesAnalysis() {
        symptomsAnalysisRepository.save(new SymptomsAnalysis.Builder()
                .setAnalysisId("SA-004").setPatient(patient).setInputText("Nausea").build());

        symptomsAnalysisRepository.deleteById("SA-004");

        assertFalse(symptomsAnalysisRepository.existsById("SA-004"));
    }

    @Test
    void findAll_returnsAllSavedAnalyses() {
        symptomsAnalysisRepository.save(new SymptomsAnalysis.Builder()
                .setAnalysisId("SA-005").setPatient(patient).setInputText("Dizziness").build());
        symptomsAnalysisRepository.save(new SymptomsAnalysis.Builder()
                .setAnalysisId("SA-006").setPatient(patient).setInputText("Fatigue").build());

        assertEquals(2, symptomsAnalysisRepository.findAll().size());
    }
}