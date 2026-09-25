package com.cput.mediqueuesystem.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.logging.Logger;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cput.mediqueuesystem.domain.Appointment;
import com.cput.mediqueuesystem.domain.Clinic;
import com.cput.mediqueuesystem.domain.Department;
import com.cput.mediqueuesystem.domain.MedicalRecord;
import com.cput.mediqueuesystem.domain.Patient;
import com.cput.mediqueuesystem.domain.Prescription;
import com.cput.mediqueuesystem.domain.Queue;
import com.cput.mediqueuesystem.domain.QueueEntry;
import com.cput.mediqueuesystem.domain.Role;
import com.cput.mediqueuesystem.domain.Staff;
import com.cput.mediqueuesystem.domain.Visit;
import com.cput.mediqueuesystem.factory.AppointmentFactory;
import com.cput.mediqueuesystem.factory.ClinicFactory;
import com.cput.mediqueuesystem.factory.DepartmentFactory;
import com.cput.mediqueuesystem.factory.MedicalRecordFactory;
import com.cput.mediqueuesystem.factory.PatientFactory;
import com.cput.mediqueuesystem.factory.PrescriptionFactory;
import com.cput.mediqueuesystem.factory.QueueEntryFactory;
import com.cput.mediqueuesystem.factory.QueueFactory;
import com.cput.mediqueuesystem.factory.RoleFactory;
import com.cput.mediqueuesystem.factory.StaffFactory;
import com.cput.mediqueuesystem.factory.VisitFactory;
import com.cput.mediqueuesystem.service.AppointmentService;
import com.cput.mediqueuesystem.service.ClinicService;
import com.cput.mediqueuesystem.service.DepartmentService;
import com.cput.mediqueuesystem.service.MedicalRecordService;
import com.cput.mediqueuesystem.service.PatientService;
import com.cput.mediqueuesystem.service.PrescriptionService;
import com.cput.mediqueuesystem.service.QueueEntryService;
import com.cput.mediqueuesystem.service.QueueService;
import com.cput.mediqueuesystem.service.RoleService;
import com.cput.mediqueuesystem.service.StaffService;
import com.cput.mediqueuesystem.service.VisitService;

/*
 * DataSeeder.java
 * Seeds the database with starter data on first startup so the
 * frontend has something real to display once it is connected to
 * the REST API. Runs only when no roles exist yet.
 *
 * Author: MediQueue
 * Date: 21 September 2026
 */
@Configuration
public class DataSeeder {

    private static final Logger LOG = Logger.getLogger(DataSeeder.class.getName());

    @Bean
    CommandLineRunner seedDatabase(RoleService roleService,
                                   ClinicService clinicService,
                                   DepartmentService departmentService,
                                   PatientService patientService,
                                   StaffService staffService,
                                   AppointmentService appointmentService,
                                   VisitService visitService,
                                   MedicalRecordService medicalRecordService,
                                   PrescriptionService prescriptionService,
                                   QueueService queueService,
                                   QueueEntryService queueEntryService) {
        return args -> {
            if (!roleService.getAllRoles().isEmpty()) {
                LOG.info("DataSeeder: database already has data, skipping seed.");
                ensureAdminStaff(roleService, departmentService, staffService);
                return;
            }

            LOG.info("DataSeeder: seeding starter data...");

            // ---- Roles ----
            Role rolePatient = roleService.createRole(RoleFactory.createRole("PATIENT"));
            Role roleReceptionist = roleService.createRole(RoleFactory.createRole("RECEPTIONIST"));
            Role roleDoctor = roleService.createRole(RoleFactory.createRole("DOCTOR"));
            Role roleNurse = roleService.createRole(RoleFactory.createRole("NURSE"));
            Role rolePharmacist = roleService.createRole(RoleFactory.createRole("PHARMACIST"));
            Role roleAdmin = roleService.createRole(RoleFactory.createRole("ADMIN"));

            // ---- Clinics ----
            clinicService.create(ClinicFactory.createClinic("CL001", "District Six Clinic", "Cape Town", "0215551001"));
            clinicService.create(ClinicFactory.createClinic("CL002", "Bellville Clinic", "Bellville", "0215551002"));
            clinicService.create(ClinicFactory.createClinic("CL003", "Khayelitsha Clinic", "Khayelitsha", "0215551003"));
            clinicService.create(ClinicFactory.createClinic("CL004", "Mitchells Plain Clinic", "Mitchells Plain", "0215551004"));

            // ---- Departments ----
            departmentService.create(DepartmentFactory.createDepartment("DEP001", "General Medicine", "General consultations"));
            departmentService.create(DepartmentFactory.createDepartment("DEP002", "Pediatrics", "Child health care"));
            departmentService.create(DepartmentFactory.createDepartment("DEP003", "Antenatal Care", "Pregnancy care"));
            departmentService.create(DepartmentFactory.createDepartment("DEP004", "HIV Care", "HIV testing and treatment"));

            Department deptGeneral = departmentService.read("DEP001");
            Department deptPediatrics = departmentService.read("DEP002");
            Department deptAntenatal = departmentService.read("DEP003");
            Department deptHiv = departmentService.read("DEP004");

            // ---- Patients ----
            LocalDateTime now = LocalDateTime.now();
            Patient p1 = patientService.create(PatientFactory.createPatient(
                    "P-0231", "Sipho", "Dlamini", "sipho.dlamini@mediqueue.co.za", "pass123",
                    "0821234567", true, now, rolePatient, "9201025800086",
                    LocalDate.of(1992, 1, 2), "M", "Cape Town", "MED-001", "Penicillin"));
            Patient p2 = patientService.create(PatientFactory.createPatient(
                    "P-0232", "Nomsa", "Khumalo", "nomsa.khumalo@mediqueue.co.za", "pass123",
                    "0715559081", true, now, rolePatient, "9903051208087",
                    LocalDate.of(1999, 3, 5), "F", "Cape Town", "MED-002", "None"));
            Patient p3 = patientService.create(PatientFactory.createPatient(
                    "P-0233", "Thabo", "Mokoena", "thabo.mokoena@mediqueue.co.za", "pass123",
                    "0638872210", true, now, rolePatient, "8107119054088",
                    LocalDate.of(1981, 7, 11), "M", "Bellville", null, "None"));
            Patient p4 = patientService.create(PatientFactory.createPatient(
                    "P-0234", "Lerato", "Molefe", "lerato.molefe@mediqueue.co.za", "pass123",
                    "0602348890", true, now, rolePatient, "0401307421089",
                    LocalDate.of(2004, 1, 30), "F", "Khayelitsha", "MED-003", "None"));
            Patient p5 = patientService.create(PatientFactory.createPatient(
                    "P-0235", "Jacob", "van Wyk", "jacob.vanwyk@mediqueue.co.za", "pass123",
                    "0827761290", true, now, rolePatient, "6809223007080",
                    LocalDate.of(1968, 9, 22), "M", "Mitchells Plain", null, "None"));
            Patient p6 = patientService.create(PatientFactory.createPatient(
                    "P-0236", "Amahle", "Ndlovu", "amahle.ndlovu@mediqueue.co.za", "pass123",
                    "0719034471", true, now, rolePatient, "9511179862081",
                    LocalDate.of(1995, 11, 17), "F", "Bellville", "MED-004", "None"));

            // ---- Staff ----
            Staff receptionist = staffService.create(StaffFactory.createStaff(
                    "U-100", "Thandi", "M.", "thandi.m@mediqueue.co.za", "pass123",
                    "0712345678", true, now, roleReceptionist, deptGeneral, "Receptionist"));
            Staff drZulu = staffService.create(StaffFactory.createStaff(
                    "U-101", "N.", "Zulu", "n.zulu@mediqueue.co.za", "pass123",
                    "0823456789", true, now, roleDoctor, deptGeneral, "Doctor"));
            Staff drPillay = staffService.create(StaffFactory.createStaff(
                    "U-102", "L.", "Pillay", "l.pillay@mediqueue.co.za", "pass123",
                    "0716667778", true, now, roleDoctor, deptPediatrics, "Doctor"));
            Staff drMthembu = staffService.create(StaffFactory.createStaff(
                    "U-103", "T.", "Mthembu", "t.mthembu@mediqueue.co.za", "pass123",
                    "0631112223", true, now, roleDoctor, deptAntenatal, "Doctor"));
            Staff drJacobs = staffService.create(StaffFactory.createStaff(
                    "U-104", "R.", "Jacobs", "r.jacobs@mediqueue.co.za", "pass123",
                    "0608889990", true, now, roleDoctor, deptHiv, "Doctor"));
            Staff pharmacist = staffService.create(StaffFactory.createStaff(
                    "U-105", "P.", "Adams", "p.adams@mediqueue.co.za", "pass123",
                    "0634567890", true, now, rolePharmacist, deptGeneral, "Pharmacist"));
            Staff nurseBongi = staffService.create(StaffFactory.createStaff(
                    "U-106", "Bongikazi", "M.", "b.mnyamana@mediqueue.co.za", "pass123",
                    "0721112233", true, now, roleNurse, deptPediatrics, "Nurse"));

            // ---- Appointments ----
            Appointment a1 = appointmentService.create(AppointmentFactory.createAppointment(
                    "APP-001", p1, drZulu, LocalDate.now(), LocalTime.of(9, 0),
                    "booked", "Checked In", receptionist));
            Appointment a2 = appointmentService.create(AppointmentFactory.createAppointment(
                    "APP-002", p2, drPillay, LocalDate.now(), LocalTime.of(10, 30),
                    "booked", "Confirmed", receptionist));
            Appointment a3 = appointmentService.create(AppointmentFactory.createAppointment(
                    "APP-003", p3, drMthembu, LocalDate.now(), LocalTime.of(11, 0),
                    "booked", "Confirmed", receptionist));
            Appointment a4 = appointmentService.create(AppointmentFactory.createAppointment(
                    "APP-004", p4, drJacobs, LocalDate.now(), LocalTime.of(12, 0),
                    "booked", "Pending", receptionist));
            Appointment a5 = appointmentService.create(AppointmentFactory.createAppointment(
                    "APP-005", p5, drZulu, LocalDate.now(), LocalTime.of(13, 0),
                    "booked", "Pending", receptionist));

            // ---- Visits ----
            visitService.create(VisitFactory.createVisit(
                    "V-1042", p1, a1, LocalDate.of(2026, 8, 1),
                    LocalTime.of(9, 5), LocalTime.of(9, 45), "Prescribed"));
            visitService.create(VisitFactory.createVisit(
                    "V-1041", p2, a2, LocalDate.of(2026, 8, 1),
                    LocalTime.of(10, 40), LocalTime.of(11, 15), "Referred"));
            visitService.create(VisitFactory.createVisit(
                    "V-1040", p3, null, LocalDate.of(2026, 7, 31),
                    LocalTime.of(11, 5), LocalTime.of(11, 40), "Follow-up"));
            visitService.create(VisitFactory.createVisit(
                    "V-1039", p4, null, LocalDate.of(2026, 7, 31),
                    LocalTime.of(12, 10), LocalTime.of(12, 55), "Prescribed"));

            // ---- Medical records ----
            MedicalRecord r1 = medicalRecordService.create(MedicalRecordFactory.createMedicalRecord(
                    "REC-001", p1, drZulu, "Hypertension", "Blood pressure elevated, prescribe medication.", LocalDate.of(2026, 8, 1)));
            MedicalRecord r2 = medicalRecordService.create(MedicalRecordFactory.createMedicalRecord(
                    "REC-002", p2, drPillay, "Asthma", "Inhaler prescribed for symptom control.", LocalDate.of(2026, 8, 1)));
            MedicalRecord r3 = medicalRecordService.create(MedicalRecordFactory.createMedicalRecord(
                    "REC-003", p3, drMthembu, "Routine antenatal", "Routine antenatal check-up.", LocalDate.of(2026, 7, 31)));

            // ---- Prescriptions ----
            prescriptionService.create(PrescriptionFactory.createPrescription(
                    "RX-501", r1, "Amlodipine 5mg", "1 tab daily", "Take once daily", LocalDate.of(2026, 8, 1)));
            prescriptionService.create(PrescriptionFactory.createPrescription(
                    "RX-502", r2, "Salbutamol Inhaler", "2 puffs as needed", "As needed for breathing", LocalDate.of(2026, 8, 1)));
            prescriptionService.create(PrescriptionFactory.createPrescription(
                    "RX-503", r3, "Folic Acid 5mg", "1 tab daily", "Take once daily", LocalDate.of(2026, 7, 31)));
            prescriptionService.create(PrescriptionFactory.createPrescription(
                    "RX-504", r2, "Tenofovir/Emtricitabine", "1 tab daily", "Take once daily", LocalDate.of(2026, 7, 31)));
            prescriptionService.create(PrescriptionFactory.createPrescription(
                    "RX-505", r1, "Paracetamol 500mg", "2 tabs 3x daily", "Take after meals", LocalDate.of(2026, 7, 30)));

            // ---- Queue ----
            Queue queue = queueService.create(QueueFactory.createQueue(
                    "QUE-001", clinicService.read("CL001"), LocalDate.now(), 50));

            queueEntryService.create(QueueEntryFactory.createQueueEntry(
                    "QE-001", queue, p1, drZulu, null, 12, "Normal", "In Consultation", LocalTime.of(8, 40)));
            queueEntryService.create(QueueEntryFactory.createQueueEntry(
                    "QE-002", queue, p2, drPillay, null, 13, "Normal", "Waiting", LocalTime.of(8, 45)));
            queueEntryService.create(QueueEntryFactory.createQueueEntry(
                    "QE-003", queue, p3, drMthembu, null, 14, "Normal", "Waiting", LocalTime.of(8, 50)));
            queueEntryService.create(QueueEntryFactory.createQueueEntry(
                    "QE-004", queue, p4, drJacobs, null, 15, "Normal", "Waiting", LocalTime.of(8, 55)));
            queueEntryService.create(QueueEntryFactory.createQueueEntry(
                    "QE-005", queue, p5, drZulu, null, 16, "Normal", "Waiting", LocalTime.of(9, 0)));

            ensureAdminStaff(roleService, departmentService, staffService);

            LOG.info("DataSeeder: seeding complete (roles, clinics, departments, patients, staff, appointments, visits, records, prescriptions, queue).");
        };
    }

    /*
     * Creates the ADMIN staff user (U-107) if it does not exist yet.
     * Runs on every startup so databases seeded before admin support
     * was added still get an administrator without a duplicate role.
     */
    private void ensureAdminStaff(RoleService roleService,
                                  DepartmentService departmentService,
                                  StaffService staffService) {
        if (staffService.read("U-107") != null) {
            return;
        }
        Role admin = roleService.getRoleByName("ADMIN");
        if (admin == null) {
            admin = roleService.createRole(RoleFactory.createRole("ADMIN"));
        }
        Department dept = departmentService.read("DEP001");
        if (dept == null) {
            dept = departmentService.create(DepartmentFactory.createDepartment(
                    "DEP001", "General Medicine", "General consultations"));
        }
        Staff adminUser = staffService.create(StaffFactory.createStaff(
                "U-107", "S.", "Ndaba", "s.ndaba@mediqueue.co.za", "pass123",
                "0723334455", true, LocalDateTime.now(), admin, dept, "Administrator"));
        if (adminUser != null) {
            LOG.info("DataSeeder: created ADMIN staff user " + adminUser.getUserId());
        }
    }
}