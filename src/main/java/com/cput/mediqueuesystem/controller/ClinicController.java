package com.cput.mediqueuesystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cput.mediqueuesystem.domain.Clinic;
import com.cput.mediqueuesystem.service.IClinicService;

/*
 * ClinicController.java
 * REST controller for managing clinics.
 *
 * Author: Uya
 * Date: 03 August 2026
 */

@RestController
@RequestMapping("/clinic")
@CrossOrigin(origins = "*")
public class ClinicController {

    private final IClinicService clinicService;

    @Autowired
    public ClinicController(IClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @PostMapping("/create")
    public ResponseEntity<Clinic> create(@RequestBody Clinic clinic) {
        Clinic created = clinicService.create(clinic);
        if (created == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<Clinic> read(@PathVariable("id") String clinicId) {
        Clinic clinic = clinicService.read(clinicId);
        if (clinic == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(clinic, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Clinic> update(@RequestBody Clinic clinic) {
        Clinic updated = clinicService.update(clinic);
        if (updated == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") String clinicId) {
        boolean deleted = clinicService.delete(clinicId);
        if (!deleted) {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Clinic>> getAll() {
        return new ResponseEntity<>(clinicService.getAll(), HttpStatus.OK);
    }
}