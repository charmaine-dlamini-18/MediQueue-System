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

import com.cput.mediqueuesystem.domain.Department;
import com.cput.mediqueuesystem.service.DepartmentService;

/*
DepartmentController.java
Department Controller
Author: Charmaine Dlamini
Date: 05 August 2026
 */
@RestController
@RequestMapping("/department")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    // Sending or storing info
    @PostMapping("/create")
    public ResponseEntity<Department> create(@RequestBody Department department) {
        Department created = departmentService.create(department);
        if (created == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Retrieving a specific department by its ID
    @GetMapping("/read/{departmentId}")
    public ResponseEntity<Department> read(@PathVariable("departmentId") String departmentId) {
        Department department = departmentService.read(departmentId);
        if (department == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(department, HttpStatus.OK);
    }

    // Updating an existing department
    @PutMapping("/update")
    public ResponseEntity<Department> update(@RequestBody Department department) {
        Department updated = departmentService.update(department);
        if (updated == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // Deleting a department by its ID
    @DeleteMapping("/delete/{departmentId}")
    public ResponseEntity<Boolean> delete(@PathVariable String departmentId) {
        boolean deleted = departmentService.delete(departmentId);
        if (!deleted) {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    // Retrieving all departments
    @GetMapping("/getAll")
    public ResponseEntity<List<Department>> getAll() {
        return new ResponseEntity<>(departmentService.getAll(), HttpStatus.OK);
    }
}