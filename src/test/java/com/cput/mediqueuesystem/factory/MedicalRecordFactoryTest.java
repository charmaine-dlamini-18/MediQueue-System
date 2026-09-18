package com.cput.mediqueuesystem.factory;


import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.cput.mediqueuesystem.domain.Patient;
import com.cput.mediqueuesystem.domain.Staff;

class MedicalRecordFactoryTest {

    private Patient patient() {
        return new Patient.Builder().setCreatedAt(LocalDateTime.now()).setUserId("P-001").build();
    }

    private Staff staff() {
        return new Staff.Builder().setUserId("U-001").build();
    }

    @Test
    void createMedicalRecord_withValidData_returnsMedicalRecord() {
        var record = MedicalRecordFactory.createMedicalRecord(
                "MR-001", patient(), staff(), "Flu", "Prescribed rest", LocalDate.of(2026, 5, 7));

        assertNotNull(record);
        assertEquals("MR-001", record.getRecordId());
        assertEquals("Flu", record.getDiagnosis());
    }

    @Test
    void createMedicalRecord_withNullRecordId_returnsNull() {
        var record = MedicalRecordFactory.createMedicalRecord(
                null, patient(), staff(), "Flu", "notes", LocalDate.now());

        assertNull(record);
    }

    @Test
    void createMedicalRecord_withBlankRecordId_returnsNull() {
        var record = MedicalRecordFactory.createMedicalRecord(
                "   ", patient(), staff(), "Flu", "notes", LocalDate.now());

        assertNull(record);
    }

    @Test
    void createMedicalRecord_withNullPatient_returnsNull() {
        var record = MedicalRecordFactory.createMedicalRecord(
                "MR-002", null, staff(), "Flu", "notes", LocalDate.now());

        assertNull(record);
    }

    @Test
    void createMedicalRecord_withNullCreatedBy_returnsNull() {
        var record = MedicalRecordFactory.createMedicalRecord(
                "MR-003", patient(), null, "Flu", "notes", LocalDate.now());

        assertNull(record);
    }

    @Test
    void createMedicalRecord_withNullRecordDate_returnsNull() {
        var record = MedicalRecordFactory.createMedicalRecord(
                "MR-004", patient(), staff(), "Flu", "notes", null);

        assertNull(record);
    }

    @Test
    void createMedicalRecord_withNullDiagnosisAndNotes_stillReturnsRecord() {
        // diagnosis and notes are optional
        var record = MedicalRecordFactory.createMedicalRecord(
                "MR-005", patient(), staff(), null, null, LocalDate.now());

        assertNotNull(record);
    }
}