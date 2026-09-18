package com.cput.mediqueuesystem.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.cput.mediqueuesystem.domain.Clinic;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
class ClinicRepositoryTest {

    @Autowired
    private ClinicRepository clinicRepository;

    @Test
    void save_persistsClinic() {
        Clinic clinic = new Clinic.Builder().setClinicId("C-001").setClinicName("District Six Clinic").build();

        Clinic saved = clinicRepository.save(clinic);

        assertEquals("C-001", saved.getClinicId());
    }

    @Test
    void findById_whenClinicExists_returnsClinic() {
        clinicRepository.save(new Clinic.Builder().setClinicId("C-002").setClinicName("Bellville Clinic").build());

        Optional<Clinic> found = clinicRepository.findById("C-002");

        assertTrue(found.isPresent());
    }

    @Test
    void findById_whenClinicDoesNotExist_returnsEmpty() {
        assertFalse(clinicRepository.findById("C-999").isPresent());
    }

    @Test
    void existsById_afterSave_returnsTrue() {
        clinicRepository.save(new Clinic.Builder().setClinicId("C-003").setClinicName("Parow Clinic").build());

        assertTrue(clinicRepository.existsById("C-003"));
    }

    @Test
    void deleteById_removesClinic() {
        clinicRepository.save(new Clinic.Builder().setClinicId("C-004").setClinicName("Goodwood Clinic").build());

        clinicRepository.deleteById("C-004");

        assertFalse(clinicRepository.existsById("C-004"));
    }

    @Test
    void findAll_returnsAllSavedClinics() {
        clinicRepository.save(new Clinic.Builder().setClinicId("C-005").setClinicName("Athlone Clinic").build());
        clinicRepository.save(new Clinic.Builder().setClinicId("C-006").setClinicName("Mitchells Plain Clinic").build());

        assertEquals(2, clinicRepository.findAll().size());
    }
}