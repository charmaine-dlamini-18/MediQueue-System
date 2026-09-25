package com.cput.mediqueuesystem.service;

import java.util.List;

import com.cput.mediqueuesystem.domain.Prescription;

/*
 * IPrescriptionService.java
 * Service contract for Prescription business logic.
 *
 * Author: Uya
 * Date: 03 August 2026
 */

public interface IPrescriptionService extends IService<Prescription, String> {

    List<Prescription> getAll();

    Prescription updateStatus(String prescriptionId, String status);
}
