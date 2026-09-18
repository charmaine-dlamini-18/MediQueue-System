package com.cput.mediqueuesystem.repository;

import com.cput.mediqueuesystem.domain.Patient;
import com.cput.mediqueuesystem.domain.Role;
import com.cput.mediqueuesystem.domain.Visit;
import com.cput.mediqueuesystem.domain.VitalSigns;
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
class VitalSignsRepositoryTest {

    @Autowired
    private VitalSignsRepository vitalSignsRepository;

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PatientRepository patientRepository;

    private VitalSigns testVitalSigns;

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

        Visit visit = visitRepository.save(new Visit.Builder()
                .setVisitId("V-001")
                .setPatient(patient)
                .setStatus("PENDING")
                .setVisitDate(LocalDate.now())
                .setCheckInTime(LocalTime.now())
                .build());

        testVitalSigns = new VitalSigns.Builder()
                .setVitalId("VT-001")
                .setVisit(visit)
                .setTemperature("36.8")
                .setBloodPressure("120/80")
                .setHeartRate("72")
                .setWeight("75.0")
                .setRecordedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void saveVitalSigns_ShouldPersist() {
        VitalSigns saved = vitalSignsRepository.save(testVitalSigns);
        assertNotNull(saved);
        assertNotNull(saved.getVitalId());
        assertEquals("36.8", saved.getTemperature());
    }

    @Test
    void findById_WhenExists_ShouldReturnVitalSigns() {
        VitalSigns saved = vitalSignsRepository.save(testVitalSigns);
        Optional<VitalSigns> found = vitalSignsRepository.findById(saved.getVitalId());
        assertTrue(found.isPresent());
        assertEquals(saved.getVitalId(), found.get().getVitalId());
    }

    @Test
    void findById_WhenDoesNotExist_ShouldReturnEmpty() {
        Optional<VitalSigns> found = vitalSignsRepository.findById("999");
        assertFalse(found.isPresent());
    }

    @Test
    void findAll_ShouldReturnAll() {
        vitalSignsRepository.save(testVitalSigns);
        List<VitalSigns> vitals = vitalSignsRepository.findAll();
        assertFalse(vitals.isEmpty());
    }

    @Test
    void deleteById_ShouldRemove() {
        VitalSigns saved = vitalSignsRepository.save(testVitalSigns);
        vitalSignsRepository.deleteById(saved.getVitalId());
        Optional<VitalSigns> found = vitalSignsRepository.findById(saved.getVitalId());
        assertFalse(found.isPresent());
    }

    @Test
    void existsById_AfterSave_ShouldReturnTrue() {
        VitalSigns saved = vitalSignsRepository.save(testVitalSigns);
        assertTrue(vitalSignsRepository.existsById(saved.getVitalId()));
    }
}