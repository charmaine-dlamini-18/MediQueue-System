package com.cput.mediqueuesystem.service;

import java.util.List;

import com.cput.mediqueuesystem.domain.Appointment;

/*
 * IAppointmentService.java
 * Service contract for Appointment business logic.
 *
 * Author: Uya
 * Date: 03 August 2026
 */

public interface IAppointmentService extends IService<Appointment, String> {

    List<Appointment> getAll();

    List<Appointment> getByPatient(String patientId);

    List<Appointment> getByDoctor(String doctorId);

    Appointment assignDoctor(String appointmentId, String doctorId);

    Appointment updateStatus(String appointmentId, String status);
}
