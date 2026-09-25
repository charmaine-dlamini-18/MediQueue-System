package com.cput.mediqueuesystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cput.mediqueuesystem.domain.VitalSigns;
import com.cput.mediqueuesystem.factory.VitalSignsFactory;
import com.cput.mediqueuesystem.repository.VitalSignsRepository;

/*
 * VitalSignsService.java
 * Implements the business logic for managing vital signs.
 *
 * Author: Uya
 * Date: 03 August 2026
 */

@Service
public class VitalSignsService implements IVitalSignsService {

    private final VitalSignsRepository vitalSignsRepository;

    @Autowired
    public VitalSignsService(VitalSignsRepository vitalSignsRepository) {
        this.vitalSignsRepository = vitalSignsRepository;
    }

    @Override
    public VitalSigns create(VitalSigns vitalSigns) {
        if (vitalSigns == null) {
            return null;
        }
        VitalSigns validated = VitalSignsFactory.createVitalSigns(
                vitalSigns.getVitalId(), vitalSigns.getVisit(), vitalSigns.getTemperature(),
                vitalSigns.getBloodPressure(), vitalSigns.getHeartRate(), vitalSigns.getWeight(),
                vitalSigns.getOxygenSaturation(), vitalSigns.getRespiratoryRate(),
                vitalSigns.getSymptoms(), vitalSigns.getNurseNotes(),
                vitalSigns.getRecordedBy(), vitalSigns.getRecordedAt());
        if (validated == null) {
            return null;
        }
        return vitalSignsRepository.save(validated);
    }

    @Override
    public VitalSigns read(String vitalId) {
        return vitalSignsRepository.findById(vitalId).orElse(null);
    }

    @Override
    public VitalSigns update(VitalSigns vitalSigns) {
        if (!vitalSignsRepository.existsById(vitalSigns.getVitalId())) {
            return null;
        }
        return vitalSignsRepository.save(vitalSigns);
    }

    @Override
    public void delete(String vitalId) {
        vitalSignsRepository.deleteById(vitalId);
    }

    @Override
    public List<VitalSigns> getAll() {
        return vitalSignsRepository.findAll();
    }
}
