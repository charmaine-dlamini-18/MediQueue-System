package com.cput.mediqueuesystem.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cput.mediqueuesystem.domain.Appointment;
import com.cput.mediqueuesystem.domain.Patient;
import com.cput.mediqueuesystem.domain.Queue;
import com.cput.mediqueuesystem.domain.QueueEntry;
import com.cput.mediqueuesystem.domain.Staff;
import com.cput.mediqueuesystem.factory.AppointmentFactory;
import com.cput.mediqueuesystem.factory.QueueEntryFactory;
import com.cput.mediqueuesystem.factory.QueueFactory;
import com.cput.mediqueuesystem.repository.AppointmentRepository;
import com.cput.mediqueuesystem.repository.QueueEntryRepository;
import com.cput.mediqueuesystem.repository.QueueRepository;
import com.cput.mediqueuesystem.repository.StaffRepository;

/*
 * AppointmentServiceImpl.java
 * Implements the business logic for managing appointments.
 *
 * Author: Uya
 * Date: 03 August 2026
 */

@Service
public class AppointmentService implements IAppointmentService {

    private static final List<String> OPEN_QUEUE_STATUSES = Arrays.asList("Waiting", "Pending", "In Consultation");

    private final AppointmentRepository appointmentRepository;
    private final StaffRepository staffRepository;
    private final QueueRepository queueRepository;
    private final QueueEntryRepository queueEntryRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository,
                              StaffRepository staffRepository,
                              QueueRepository queueRepository,
                              QueueEntryRepository queueEntryRepository) {
        this.appointmentRepository = appointmentRepository;
        this.staffRepository = staffRepository;
        this.queueRepository = queueRepository;
        this.queueEntryRepository = queueEntryRepository;
    }

    @Override
    public Appointment create(Appointment appointment) {
        if (appointment == null) {
            return null;
        }
        Appointment validated = AppointmentFactory.createAppointment(
                appointment.getAppointmentId(), appointment.getPatient(), appointment.getDoctor(),
                appointment.getScheduledDate(), appointment.getScheduledTime(),
                appointment.getAppointmentType(), appointment.getStatus(), appointment.getCreatedBy());
        if (validated == null) {
            return null;
        }
        validated.setClinic(appointment.getClinic());
        validated.setDepartment(appointment.getDepartment());
        validated.setReason(appointment.getReason());
        Appointment saved = appointmentRepository.save(validated);
        ensureQueueEntry(saved);
        return saved;
    }

    /*
     * Gives the patient a queue number the moment their booking is created
     * (before any doctor is assigned). Opens/closes an entry for the clinic's
     * queue on the booking date and never creates duplicates.
     */
    private void ensureQueueEntry(Appointment appointment) {
        try {
            Patient patient = appointment.getPatient();
            if (patient == null || appointment.getClinic() == null || appointment.getClinic().getClinicId() == null) {
                return;
            }
            LocalDate date = appointment.getScheduledDate() != null ? appointment.getScheduledDate() : LocalDate.now();

            Queue queue = queueRepository
                    .findByClinicClinicIdAndDate(appointment.getClinic().getClinicId(), date)
                    .orElseGet(() -> queueRepository.save(QueueFactory.createQueue(
                            "QUE-" + System.currentTimeMillis(),
                            appointment.getClinic(), date, 50)));

            if (queueEntryRepository.findFirstByPatientUserIdAndStatusIn(
                    patient.getUserId(), OPEN_QUEUE_STATUSES).isPresent()) {
                return; // already queued
            }

            Integer max = queueEntryRepository.findByQueueQueueId(queue.getQueueId()).stream()
                    .map(QueueEntry::getQueueNumber)
                    .max(Integer::compareTo)
                    .orElse(0);

            QueueEntry entry = QueueEntryFactory.createQueueEntry(
                    "QE-" + System.currentTimeMillis(), queue, patient, null, null,
                    max + 1, "Normal", "Waiting", LocalTime.now());
            if (entry != null) {
                queueEntryRepository.save(entry);
            }
        } catch (Exception e) {
            // Queue assignment must never break the booking itself.
        }
    }

    @Override
    public Appointment read(String appointmentId) {
        return appointmentRepository.findById(appointmentId).orElse(null);
    }

    @Override
    public Appointment update(Appointment appointment) {
        if (!appointmentRepository.existsById(appointment.getAppointmentId())) {
            return null;
        }
        return appointmentRepository.save(appointment);
    }

    @Override
    public boolean delete(String appointmentId) {
        if (!appointmentRepository.existsById(appointmentId)) {
            return false;
        }
        appointmentRepository.deleteById(appointmentId);
        return true;
    }

    @Override
    public List<Appointment> getAll() {
        return appointmentRepository.findAll();
    }

    @Override
    public List<Appointment> getByPatient(String patientId) {
        return appointmentRepository.findByPatientUserId(patientId);
    }

    @Override
    public List<Appointment> getByDoctor(String doctorId) {
        return appointmentRepository.findByDoctorUserId(doctorId);
    }

    @Override
    public Appointment assignDoctor(String appointmentId, String doctorId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointment == null) {
            return null;
        }
        Staff doctor = staffRepository.findById(doctorId).orElse(null);
        if (doctor == null) {
            return null;
        }
        appointment.setDoctor(doctor);
        appointment.setStatus("Confirmed");
        Appointment saved = appointmentRepository.save(appointment);

        // If the patient was queued without a doctor, attach the assigned
        // doctor to their open queue entry too.
        Patient patient = saved.getPatient();
        if (patient != null) {
            queueEntryRepository.findByPatientUserIdAndStatusIn(patient.getUserId(), OPEN_QUEUE_STATUSES)
                    .forEach(entry -> {
                        entry.setDoctor(doctor);
                        queueEntryRepository.save(entry);
                    });
        }
        return saved;
    }

    @Override
    public Appointment updateStatus(String appointmentId, String status) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointment == null) {
            return null;
        }
        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }
}