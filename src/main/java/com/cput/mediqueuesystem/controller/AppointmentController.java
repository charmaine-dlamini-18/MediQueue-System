package com.cput.mediqueuesystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cput.mediqueuesystem.domain.Appointment;
import com.cput.mediqueuesystem.service.IAppointmentService;

/*
 * AppointmentController.java
 * REST controller for managing appointments.
 *
 * Author: Uya
 * Date: 03 August 2026
 */

@RestController
@RequestMapping("/appointment")
@CrossOrigin(origins = "*")
public class AppointmentController {

    private final IAppointmentService appointmentService;

    @Autowired
    public AppointmentController(IAppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/create")
    public Appointment create(@RequestBody Appointment appointment) {
        return appointmentService.create(appointment);
    }

    @GetMapping("/read/{id}")
    public Appointment read(@PathVariable("id") String appointmentId) {
        return appointmentService.read(appointmentId);
    }

    @PutMapping("/update")
    public Appointment update(@RequestBody Appointment appointment) {
        return appointmentService.update(appointment);
    }

    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable("id") String appointmentId) {
        return appointmentService.delete(appointmentId);
    }

    @GetMapping("/getAll")
    public List<Appointment> getAll() {
        return appointmentService.getAll();
    }

    // Appointments for a specific patient (used by the patient dashboard)
    @GetMapping("/patient/{patientId}")
    public List<Appointment> getByPatient(@PathVariable("patientId") String patientId) {
        return appointmentService.getByPatient(patientId);
    }

    // Appointments assigned to a specific doctor (used by the doctor dashboard)
    @GetMapping("/doctor/{doctorId}")
    public List<Appointment> getByDoctor(@PathVariable("doctorId") String doctorId) {
        return appointmentService.getByDoctor(doctorId);
    }

    // Assign a doctor to an appointment (admin/doctor/receptionist)
    @PutMapping("/assign/{appointmentId}/{doctorId}")
    public Appointment assignDoctor(@PathVariable("appointmentId") String appointmentId,
                                    @PathVariable("doctorId") String doctorId) {
        return appointmentService.assignDoctor(appointmentId, doctorId);
    }

    // Update the status of an appointment (admin/doctor/receptionist)
    @PutMapping("/status/{appointmentId}/{status}")
    public Appointment updateStatus(@PathVariable("appointmentId") String appointmentId,
                                    @PathVariable("status") String status) {
        return appointmentService.updateStatus(appointmentId, status);
    }
}
