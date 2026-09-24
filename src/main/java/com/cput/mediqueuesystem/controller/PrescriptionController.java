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

import com.cput.mediqueuesystem.domain.Prescription;
import com.cput.mediqueuesystem.service.IPrescriptionService;

/*
 * PrescriptionController.java
 * REST controller for managing prescriptions.
 *
 * Author: Uya
 * Date: 03 August 2026
 */

@RestController
@RequestMapping("/prescription")
@CrossOrigin(origins = "*")
public class PrescriptionController {

    private final IPrescriptionService prescriptionService;

    @Autowired
    public PrescriptionController(IPrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @PostMapping("/create")
    public Prescription create(@RequestBody Prescription prescription) {
        return prescriptionService.create(prescription);
    }

    @GetMapping("/read/{id}")
    public Prescription read(@PathVariable("id") String prescriptionId) {
        return prescriptionService.read(prescriptionId);
    }

    @PutMapping("/update")
    public Prescription update(@RequestBody Prescription prescription) {
        return prescriptionService.update(prescription);
    }

    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable("id") String prescriptionId) {
        return prescriptionService.delete(prescriptionId);
    }

    @GetMapping("/getAll")
    public List<Prescription> getAll() {
        return prescriptionService.getAll();
    }

    @PutMapping("/status/{id}/{status}")
    public Prescription updateStatus(@PathVariable("id") String prescriptionId,
                                     @PathVariable("status") String status) {
        return prescriptionService.updateStatus(prescriptionId, status);
    }
}
