package com.cput.mediqueuesystem.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.cput.mediqueuesystem.domain.Role;

class PatientFactoryTest {

    private Role role() {
        return new Role.Builder().setRoleId(1L).setRoleName("Patient").build();
    }

    @Test
    void createPatient_withValidData_returnsPatient() {
        var patient = PatientFactory.createPatient(
                "P-001", "Ellen", "Luella", "ellen@example.com", "pass123",
                "0821234567", true, LocalDateTime.now(), role(),
                "9001015800081", LocalDate.of(1990, 1, 1), "Female",
                "123 Main Street", "MA-001", "Penicillin");

        assertNotNull(patient);
        assertEquals("P-001", patient.getUserId());
        assertEquals("9001015800081", patient.getIdNumber());
    }

    @Test
    void createPatient_withInvalidIdNumber_returnsNull() {
        var patient = PatientFactory.createPatient(
                "P-002", "Ellen", "Luella", "ellen@example.com", "pass123",
                "0821234567", true, LocalDateTime.now(), role(),
                "123", LocalDate.of(1990, 1, 1), "Female",
                "123 Main Street", null, null);

        assertNull(patient);
    }

    @Test
    void createPatient_withNullDateOfBirth_returnsNull() {
        var patient = PatientFactory.createPatient(
                "P-003", "Ellen", "Luella", "ellen@example.com", "pass123",
                "0821234567", true, LocalDateTime.now(), role(),
                "9001015800081", null, "Female", "123 Main Street", null, null);

        assertNull(patient);
    }

    @Test
    void createPatient_withNullGender_returnsNull() {
        var patient = PatientFactory.createPatient(
                "P-004", "Ellen", "Luella", "ellen@example.com", "pass123",
                "0821234567", true, LocalDateTime.now(), role(),
                "9001015800081", LocalDate.of(1990, 1, 1), null, "123 Main Street", null, null);

        assertNull(patient);
    }

    @Test
    void createPatient_withNullAddress_returnsNull() {
        var patient = PatientFactory.createPatient(
                "P-005", "Ellen", "Luella", "ellen@example.com", "pass123",
                "0821234567", true, LocalDateTime.now(), role(),
                "9001015800081", LocalDate.of(1990, 1, 1), "Female", null, null, null);

        assertNull(patient);
    }

    @Test
    void createPatient_withInvalidEmail_returnsNull() {
        var patient = PatientFactory.createPatient(
                "P-006", "Ellen", "Luella", "not-an-email", "pass123",
                "0821234567", true, LocalDateTime.now(), role(),
                "9001015800081", LocalDate.of(1990, 1, 1), "Female", "123 Main Street", null, null);

        assertNull(patient);
    }

    @Test
    void createPatient_withNullMedicalAidAndAllergies_stillReturnsPatient() {
        // optional fields
        var patient = PatientFactory.createPatient(
                "P-007", "Ellen", "Luella", "ellen@example.com", "pass123",
                "0821234567", true, LocalDateTime.now(), role(),
                "9001015800081", LocalDate.of(1990, 1, 1), "Female", "123 Main Street", null, null);

        assertNotNull(patient);
    }
}
