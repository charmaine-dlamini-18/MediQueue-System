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

import com.cput.mediqueuesystem.domain.MedicalRecord;
import com.cput.mediqueuesystem.service.IMedicalRecordService;

/*
 * MedicalRecordController.java
 * REST controller for managing medical records.
 *
 * Author: Uya
 * Date: 03 August 2026
 */

@RestController
@RequestMapping("/medical-record")
@CrossOrigin(origins = "*")
public class MedicalRecordController {

    private final IMedicalRecordService medicalRecordService;

    @Autowired
    public MedicalRecordController(IMedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @PostMapping("/create")
    public MedicalRecord create(@RequestBody MedicalRecord medicalRecord) {
        return medicalRecordService.create(medicalRecord);
    }

    @GetMapping("/read/{id}")
    public MedicalRecord read(@PathVariable("id") String recordId) {
        return medicalRecordService.read(recordId);
    }

    @PutMapping("/update")
    public MedicalRecord update(@RequestBody MedicalRecord medicalRecord) {
        return medicalRecordService.update(medicalRecord);
    }

    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable("id") String recordId) {
        return medicalRecordService.delete(recordId);
    }

    @GetMapping("/getAll")
    public List<MedicalRecord> getAll() {
        return medicalRecordService.getAll();
    }

    @GetMapping("/patient/{patientId}")
    public List<MedicalRecord> getByPatient(@PathVariable("patientId") String patientId) {
        return medicalRecordService.getByPatient(patientId);
    }
}
