package com.cput.mediqueuesystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cput.mediqueuesystem.domain.Prescription;
import com.cput.mediqueuesystem.factory.PrescriptionFactory;
import com.cput.mediqueuesystem.repository.PrescriptionRepository;

/*
 * PrescriptionServiceImpl.java
 * Implements the business logic for managing prescriptions.
 *
 * Author: Uya
 * Date: 03 August 2026
 */

@Service
public class PrescriptionService implements IPrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    @Autowired
    public PrescriptionService(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    @Override
    public Prescription create(Prescription prescription) {
        if (prescription == null) {
            return null;
        }
        Prescription validated = PrescriptionFactory.createPrescription(
                prescription.getPrescriptionId(), prescription.getMedicalRecord(),
                prescription.getMedicationName(), prescription.getDosage(),
                prescription.getInstructions(), prescription.getPrescriptionDate());
        if (validated == null) {
            return null;
        }
        return prescriptionRepository.save(validated);
    }

    @Override
    public Prescription read(String prescriptionId) {
        return prescriptionRepository.findById(prescriptionId).orElse(null);
    }

    @Override
    public Prescription update(Prescription prescription) {
        if (!prescriptionRepository.existsById(prescription.getPrescriptionId())) {
            return null;
        }
        return prescriptionRepository.save(prescription);
    }

    @Override
    public boolean delete(String prescriptionId) {
        if (!prescriptionRepository.existsById(prescriptionId)) {
            return false;
        }
        prescriptionRepository.deleteById(prescriptionId);
        return true;
    }

    @Override
    public List<Prescription> getAll() {
        return prescriptionRepository.findAll();
    }

    @Override
    public Prescription updateStatus(String prescriptionId, String status) {
        Prescription prescription = prescriptionRepository.findById(prescriptionId).orElse(null);
        if (prescription == null || status == null || status.isBlank()) {
            return null;
        }
        prescription.setStatus(status);
        return prescriptionRepository.save(prescription);
    }
}
