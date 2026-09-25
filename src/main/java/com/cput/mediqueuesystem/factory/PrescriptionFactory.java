package com.cput.mediqueuesystem.factory;

import java.time.LocalDate;

import org.apache.commons.validator.GenericValidator;

import com.cput.mediqueuesystem.domain.MedicalRecord;
import com.cput.mediqueuesystem.domain.Prescription;

/*
 * PrescriptionFactory.java
 * Validates input and builds Prescription objects. Returns null
 * if required fields are missing or invalid, so callers can check
 * for a null result before persisting.
 *
 * Author: Uya
 * Date: 03 August 2026
 */

public class PrescriptionFactory {

    public static Prescription createPrescription(String prescriptionId, MedicalRecord medicalRecord,
                                                    String medicationName, String dosage,
                                                    String instructions, LocalDate prescriptionDate) {

        if (GenericValidator.isBlankOrNull(prescriptionId)
                || medicalRecord == null
                || GenericValidator.isBlankOrNull(medicationName)
                || prescriptionDate == null) {
            return null;
        }

        return new Prescription.Builder()
                .setPrescriptionId(prescriptionId)
                .setMedicalRecord(medicalRecord)
                .setMedicationName(medicationName)
                .setDosage(dosage)
                .setInstructions(instructions)
                .setPrescriptionDate(prescriptionDate)
                .setStatus("Pending")
                .build();
    }
}