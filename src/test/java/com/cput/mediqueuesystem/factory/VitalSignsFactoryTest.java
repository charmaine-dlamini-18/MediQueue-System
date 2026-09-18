package com.cput.mediqueuesystem.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.cput.mediqueuesystem.domain.Patient;
import com.cput.mediqueuesystem.domain.Visit;

class VitalSignsFactoryTest {

    private Visit buildVisit() {
        Patient patient = new Patient.Builder().setCreatedAt(LocalDateTime.now()).setUserId("P-001").build();
        return new Visit.Builder()
                .setVisitId("V-001")
                .setPatient(patient)
                .setVisitDate(java.time.LocalDate.of(2026, 5, 7))
                .setStatus("Pending")
                .build();
    }

    @Test
    void createVitalSigns_withValidData_returnsVitalSigns() {
        Visit visit = buildVisit();

        com.cput.mediqueuesystem.domain.VitalSigns vitals = VitalSignsFactory.createVitalSigns(
                "VS-0001", visit, "36.8", "117/76", "78", "80",
                null, LocalDateTime.of(2026, 5, 6, 9, 15));

        assertNotNull(vitals);
        assertEquals("VS-0001", vitals.getVitalId());
        assertEquals(visit, vitals.getVisit());
        assertEquals("36.8", vitals.getTemperature());
        assertEquals("117/76", vitals.getBloodPressure());
        assertEquals("78", vitals.getHeartRate());
        assertEquals("80", vitals.getWeight());
    }

    @Test
    void createVitalSigns_withNullVitalId_returnsNull() {
        Visit visit = buildVisit();

        com.cput.mediqueuesystem.domain.VitalSigns vitals = VitalSignsFactory.createVitalSigns(
                null, visit, "36.8", "117/76", "78", "80", null, LocalDateTime.now());

        assertNull(vitals);
    }

    @Test
    void createVitalSigns_withBlankVitalId_returnsNull() {
        Visit visit = buildVisit();

        com.cput.mediqueuesystem.domain.VitalSigns vitals = VitalSignsFactory.createVitalSigns(
                "   ", visit, "36.8", "117/76", "78", "80", null, LocalDateTime.now());

        assertNull(vitals);
    }

    @Test
    void createVitalSigns_withNullVisit_returnsNull() {
        com.cput.mediqueuesystem.domain.VitalSigns vitals = VitalSignsFactory.createVitalSigns(
                "VS-0002", null, "36.8", "117/76", "78", "80", null, LocalDateTime.now());

        assertNull(vitals);
    }

    @Test
    void createVitalSigns_withNullTemperature_stillReturnsVitalSigns() {
        // temperature is not validated as required by the factory
        Visit visit = buildVisit();

        com.cput.mediqueuesystem.domain.VitalSigns vitals = VitalSignsFactory.createVitalSigns(
                "VS-0003", visit, null, "117/76", "78", "80", null, LocalDateTime.now());

        assertNotNull(vitals);
    }

    @Test
    void createVitalSigns_withNullRecordedBy_stillReturnsVitalSigns() {
        // recordedBy is optional
        Visit visit = buildVisit();

        com.cput.mediqueuesystem.domain.VitalSigns vitals = VitalSignsFactory.createVitalSigns(
                "VS-0004", visit, "36.8", "117/76", "78", "80", null, null);

        assertNotNull(vitals);
        assertNull(vitals.getRecordedBy());
        assertNull(vitals.getRecordedAt());
    }
}