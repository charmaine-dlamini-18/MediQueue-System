package com.cput.mediqueuesystem.factory;


import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import com.cput.mediqueuesystem.domain.Appointment;
import com.cput.mediqueuesystem.domain.Patient;
import com.cput.mediqueuesystem.domain.Visit;

class VisitFactoryTest {

    @Test
    void createVisit_withValidData_returnsVisit() {
        Patient patient = new Patient.Builder().setCreatedAt(LocalDateTime.now()).setUserId("P-001").build();

        Visit visit = VisitFactory.createVisit(
                "V-0001", patient, null,
                LocalDate.of(2026, 5, 7),
                LocalTime.of(9, 15), null, "Pending");

        assertNotNull(visit);
        assertEquals("V-0001", visit.getVisitId());
        assertEquals(patient, visit.getPatient());
        assertEquals(LocalDate.of(2026, 5, 7), visit.getVisitDate());
        assertEquals("Pending", visit.getStatus());
    }

    @Test
    void createVisit_withAppointment_setsAppointment() {
        Patient patient = new Patient.Builder().setCreatedAt(LocalDateTime.now()).setUserId("P-001").build();
        Appointment appointment = new Appointment.Builder().setAppointmentId("A-001").build();

        Visit visit = VisitFactory.createVisit(
                "V-0002", patient, appointment,
                LocalDate.of(2026, 5, 7), LocalTime.of(10, 0), null, "Pending");

        assertNotNull(visit);
        assertEquals(appointment, visit.getAppointment());
    }

    @Test
    void createVisit_withNullVisitId_returnsNull() {
        Patient patient = new Patient.Builder().setCreatedAt(LocalDateTime.now()).setUserId("P-001").build();

        Visit visit = VisitFactory.createVisit(
                null, patient, null, LocalDate.now(), LocalTime.now(), null, "Pending");

        assertNull(visit);
    }

    @Test
    void createVisit_withBlankVisitId_returnsNull() {
        Patient patient = new Patient.Builder().setCreatedAt(LocalDateTime.now()).setUserId("P-001").build();

        Visit visit = VisitFactory.createVisit(
                "   ", patient, null, LocalDate.now(), LocalTime.now(), null, "Pending");

        assertNull(visit);
    }

    @Test
    void createVisit_withNullPatient_returnsNull() {
        Visit visit = VisitFactory.createVisit(
                "V-0003", null, null, LocalDate.now(), LocalTime.now(), null, "Pending");

        assertNull(visit);
    }

    @Test
    void createVisit_withNullVisitDate_returnsNull() {
        Patient patient = new Patient.Builder().setCreatedAt(LocalDateTime.now()).setUserId("P-001").build();

        Visit visit = VisitFactory.createVisit(
                "V-0004", patient, null, null, LocalTime.now(), null, "Pending");

        assertNull(visit);
    }

    @Test
    void createVisit_withNullCheckInTime_stillReturnsVisit() {
        // checkInTime is not validated as required by the factory
        Patient patient = new Patient.Builder().setCreatedAt(LocalDateTime.now()).setUserId("P-001").build();

        Visit visit = VisitFactory.createVisit(
                "V-0005", patient, null, LocalDate.now(), null, null, "Pending");

        assertNotNull(visit);
    }
}