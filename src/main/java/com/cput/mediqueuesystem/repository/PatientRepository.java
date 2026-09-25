package com.cput.mediqueuesystem.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cput.mediqueuesystem.domain.Patient;

/*
 * PatientRepository.java
 * JPA repository for Patient entities.
 *
 * Author: Charmaine Dlamini
 * Date: 05 August 2026
 */

@Repository
public interface PatientRepository extends JpaRepository<Patient, String> {
    // Inherits CRUD operations (create, read, update, delete) from JpaRepository

    Optional<Patient> findByIdNumber(String idNumber);

    Optional<Patient> findByEmail(String email);
}
