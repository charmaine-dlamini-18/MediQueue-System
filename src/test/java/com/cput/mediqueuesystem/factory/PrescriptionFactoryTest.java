package com.cput.mediqueuesystem.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.cput.mediqueuesystem.domain.MedicalRecord;

class PrescriptionFactoryTest {

    private MedicalRecord medicalRecord() {
        return new MedicalRecord.Builder().setRecordId("MR-001").build();
    }

    @Test
    void createPrescription_withValidData_returnsPrescription() {
        var prescription = PrescriptionFactory.createPrescription(
                "RX-001", medicalRecord(), "Paracetamol", "500mg",
                "Twice daily after meals", LocalDate.of(2026, 5, 7));

        assertNotNull(prescription);
        assertEquals("RX-001", prescription.getPrescriptionId());
        assertEquals("Paracetamol", prescription.getMedicationName());
    }

    @Test
    void createPrescription_withNullPrescriptionId_returnsNull() {
        var prescription = PrescriptionFactory.createPrescription(
                null, medicalRecord(), "Paracetamol", "500mg", "instructions", LocalDate.now());

        assertNull(prescription);
    }

    @Test
    void createPrescription_withBlankPrescriptionId_returnsNull() {
        var prescription = PrescriptionFactory.createPrescription(
                "   ", medicalRecord(), "Paracetamol", "500mg", "instructions", LocalDate.now());

        assertNull(prescription);
    }

    @Test
    void createPrescription_withNullMedicalRecord_returnsNull() {
        var prescription = PrescriptionFactory.createPrescription(
                "RX-002", null, "Paracetamol", "500mg", "instructions", LocalDate.now());

        assertNull(prescription);
    }

    @Test
    void createPrescription_withNullMedicationName_returnsNull() {
        var prescription = PrescriptionFactory.createPrescription(
                "RX-003", medicalRecord(), null, "500mg", "instructions", LocalDate.now());

        assertNull(prescription);
    }

    @Test
    void createPrescription_withNullPrescriptionDate_returnsNull() {
        var prescription = PrescriptionFactory.createPrescription(
                "RX-004", medicalRecord(), "Paracetamol", "500mg", "instructions", null);

        assertNull(prescription);
    }

    @Test
    void createPrescription_withNullDosageAndInstructions_stillReturnsPrescription() {
        // dosage and instructions are optional
        var prescription = PrescriptionFactory.createPrescription(
                "RX-005", medicalRecord(), "Paracetamol", null, null, LocalDate.now());

        assertNotNull(prescription);
    }
}