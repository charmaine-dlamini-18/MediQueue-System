package com.cput.mediqueuesystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cput.mediqueuesystem.domain.MedicalRecord;

/*
 * MedicalRecordRepository.java
 * JPA repository for MedicalRecord entities.
 *
 * Author: Uya
 * Date: 03 August 2026
 */

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, String> {

    List<MedicalRecord> findByPatientUserId(String patientId);
}
