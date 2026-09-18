package com.cput.mediqueuesystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cput.mediqueuesystem.domain.Staff;
import com.cput.mediqueuesystem.service.StaffService;

/*
StaffController.java
Staff Controller
Author: Charmaine Dlamini
Date: 05 August 2026
 */
@RestController
@RequestMapping("/staff")
public class StaffController {

    private final StaffService staffService;

    @Autowired
    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    // Sending or storing info
    @PostMapping("/create")
    public ResponseEntity<Staff> create(@RequestBody Staff staff) {
        Staff created = staffService.create(staff);
        if (created == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Retrieving a specific staff by its ID
    @GetMapping("/read/{staffId}")
    public ResponseEntity<Staff> read(@PathVariable("staffId") String staffId) {
        Staff staff = staffService.read(staffId);
        if (staff == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(staff, HttpStatus.OK);
    }

    // Updating an existing staff
    @PutMapping("/update")
    public ResponseEntity<Staff> update(@RequestBody Staff staff) {
        Staff updated = staffService.update(staff);
        if (updated == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // Deleting a staff by its ID
    @DeleteMapping("/delete/{staffId}")
    public ResponseEntity<Boolean> delete(@PathVariable String staffId) {
        boolean deleted = staffService.delete(staffId);
        if (!deleted) {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    // Retrieving all staff
    @GetMapping("/getAll")
    public ResponseEntity<List<Staff>> getAll() {
        return new ResponseEntity<>(staffService.getAll(), HttpStatus.OK);
    }
}