package com.cput.mediqueuesystem.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cput.mediqueuesystem.domain.Department;
import com.cput.mediqueuesystem.repository.DepartmentRepository;

/*
DepartmentService.java
DepartmentService
Author: Charmaine Dlamini
Date: 05 August 2026
 */
@Service
public class DepartmentService implements IDepartmentService {
    private final DepartmentRepository repository;

    @Autowired
    DepartmentService(DepartmentRepository repository) {
        this.repository = repository;
    }

    // Saves a new department to the database
    @Override
    public Department create(Department department) {
        return this.repository.save(department);
    }

    // Finds a department by its department ID
    @Override
    public Department read(String departmentId) {
        return this.repository.findById(departmentId).orElse(null);
    }

    // Updates an existing department in the database
    @Override
    public Department update(Department department) {
        return this.repository.save(department);
    }

    // Deletes a department by its department ID
    @Override
    public boolean delete(String departmentId) {
        this.repository.deleteById(departmentId);
        return true;
    }

    // Returns a list of all departments
    @Override
    public List<Department> getAll() {
        return this.repository.findAll();
    }
}