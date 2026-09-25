package com.cput.mediqueuesystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cput.mediqueuesystem.domain.Appointment;

/*
 * AppointmentRepository.java
 * JPA repository for Appointment entities.
 *
 * Author: Uya
 * Date: 03 August 2026
 */

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {

    List<Appointment> findByPatientUserId(String patientId);

    List<Appointment> findByDoctorUserId(String doctorId);
}
