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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.cput.mediqueuesystem.domain.Patient;
import com.cput.mediqueuesystem.domain.Role;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
class PatientRepositoryTest {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private RoleRepository roleRepository;

    private Role role;

    @BeforeEach
    void setUp() {
        role = roleRepository.save(new Role.Builder().setRoleId(1L).setRoleName("Patient").build());
    }

    private Patient buildPatient(String userId, String idNumber) {
        return new Patient.Builder().setCreatedAt(LocalDateTime.now())
                .setUserId(userId).setFirstName("Ellen").setLastName("Luella")
                .setEmail(userId + "@example.com").setPassword("pass123")
                .setPhoneNumber("0821234567").setStatus(true).setRole(role)
                .setIdNumber(idNumber).setDateOfBirth(LocalDate.of(1990, 1, 1))
                .setGender("Female").setAddress("123 Main Street")
                .build();
    }

    @Test
    void save_persistsPatient() {
        Patient saved = patientRepository.save(buildPatient("P-001", "9001015800081"));

        assertEquals("P-001", saved.getUserId());
    }

    @Test
    void findById_whenPatientExists_returnsPatient() {
        patientRepository.save(buildPatient("P-002", "9002015800082"));

        Optional<Patient> found = patientRepository.findById("P-002");

        assertTrue(found.isPresent());
        assertEquals("Female", found.get().getGender());
    }

    @Test
    void findById_whenPatientDoesNotExist_returnsEmpty() {
        assertFalse(patientRepository.findById("P-999").isPresent());
    }

    @Test
    void existsById_afterSave_returnsTrue() {
        patientRepository.save(buildPatient("P-003", "9003015800083"));

        assertTrue(patientRepository.existsById("P-003"));
    }

    @Test
    void deleteById_removesPatient() {
        patientRepository.save(buildPatient("P-004", "9004015800084"));

        patientRepository.deleteById("P-004");

        assertFalse(patientRepository.existsById("P-004"));
    }

    @Test
    void findAll_returnsAllSavedPatients() {
        patientRepository.save(buildPatient("P-005", "9005015800085"));
        patientRepository.save(buildPatient("P-006", "9006015800086"));

        assertEquals(2, patientRepository.findAll().size());
    }

    @Test
    void save_withDuplicateIdNumber_throwsException() {
        // id_number is unique = true
        patientRepository.saveAndFlush(buildPatient("P-007", "9007015800087"));

        org.junit.jupiter.api.Assertions.assertThrows(
                org.springframework.dao.DataIntegrityViolationException.class,
                () -> patientRepository.saveAndFlush(buildPatient("P-008", "9007015800087")));
    }
}