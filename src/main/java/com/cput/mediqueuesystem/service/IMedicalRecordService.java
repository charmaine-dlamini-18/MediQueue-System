package com.cput.mediqueuesystem.service;

import java.util.List;

import com.cput.mediqueuesystem.domain.MedicalRecord;

/*
 * IMedicalRecordService.java
 * Service contract for MedicalRecord business logic.
 *
 * Author: Uya
 * Date: 03 August 2026
 */

public interface IMedicalRecordService extends IService<MedicalRecord, String> {

    List<MedicalRecord> getAll();

    List<MedicalRecord> getByPatient(String patientId);
}
