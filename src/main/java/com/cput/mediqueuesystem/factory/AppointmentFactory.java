package com.cput.mediqueuesystem.factory;

import java.time.LocalDate;
import java.time.LocalTime;

import org.apache.commons.validator.GenericValidator;

import com.cput.mediqueuesystem.domain.Appointment;
import com.cput.mediqueuesystem.domain.Patient;
import com.cput.mediqueuesystem.domain.Staff;

/*
 * AppointmentFactory.java
 * Validates input and builds Appointment objects. Returns null if
 * required fields are missing or invalid, so callers can check
 * for a null result before persisting.
 *
 * A doctor is optional at this stage: patients book without a
 * doctor and staff assign one later. The scheduled date, scheduled
 * time and patient are always required.
 *
 * Author: Uya
 * Date: 03 August 2026
 */

public class AppointmentFactory {

    public static Appointment createAppointment(String appointmentId, Patient patient, Staff doctor,
                                                  LocalDate scheduledDate, LocalTime scheduledTime,
                                                  String appointmentType, String status, Staff createdBy) {

        if (GenericValidator.isBlankOrNull(appointmentId)
                || patient == null
                || scheduledDate == null
                || scheduledTime == null) {
            return null;
        }

        return new Appointment.Builder()
                .setAppointmentId(appointmentId)
                .setPatient(patient)
                .setDoctor(doctor)
                .setScheduledDate(scheduledDate)
                .setScheduledTime(scheduledTime)
                .setAppointmentType(appointmentType)
                .setStatus(status)
                .setCreatedBy(createdBy)
                .build();
    }
}