package com.cput.mediqueuesystem.repository;

import com.cput.mediqueuesystem.domain.Patient;
import com.cput.mediqueuesystem.domain.Role;
import com.cput.mediqueuesystem.domain.Visit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class VisitRepositoryTest {

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PatientRepository patientRepository;

    private Visit testVisit;

    @BeforeEach
    void setUp() {
        Role role = roleRepository.save(new Role.Builder().setRoleName("Patient").build());

        Patient patient = patientRepository.save(new Patient.Builder()
                .setUserId("P-001").setFirstName("Ellen").setLastName("Luella")
                .setEmail("ellen@example.com").setPassword("pass123").setPhoneNumber("0821234567")
                .setStatus(true).setCreatedAt(LocalDateTime.now()).setRole(role)
                .setIdNumber("9001015800081").setDateOfBirth(LocalDate.of(1990, 1, 1))
                .setGender("Female").setAddress("123 Main Street")
                .build());

        testVisit = new Visit.Builder()
                .setVisitId("V-001")
                .setPatient(patient)
                .setStatus("PENDING")
                .setVisitDate(LocalDate.now())
                .setCheckInTime(LocalTime.now())
                .build();
    }

    @Test
    void saveVisit_ShouldPersistVisit() {
        Visit saved = visitRepository.save(testVisit);
        assertNotNull(saved);
        assertNotNull(saved.getVisitId());
        assertEquals("PENDING", saved.getStatus());
    }

    @Test
    void findById_WhenVisitExists_ShouldReturnVisit() {
        Visit saved = visitRepository.save(testVisit);
        Optional<Visit> found = visitRepository.findById(saved.getVisitId());
        assertTrue(found.isPresent());
        assertEquals(saved.getVisitId(), found.get().getVisitId());
    }

    @Test
    void findById_WhenVisitDoesNotExist_ShouldReturnEmpty() {
        Optional<Visit> found = visitRepository.findById("999");
        assertFalse(found.isPresent());
    }

    @Test
    void findAll_ShouldReturnAllVisits() {
        visitRepository.save(testVisit);
        List<Visit> visits = visitRepository.findAll();
        assertFalse(visits.isEmpty());
    }

    @Test
    void deleteById_ShouldRemoveVisit() {
        Visit saved = visitRepository.save(testVisit);
        visitRepository.deleteById(saved.getVisitId());
        Optional<Visit> found = visitRepository.findById(saved.getVisitId());
        assertFalse(found.isPresent());
    }

    @Test
    void existsById_AfterSave_ShouldReturnTrue() {
        Visit saved = visitRepository.save(testVisit);
        assertTrue(visitRepository.existsById(saved.getVisitId()));
    }
}