package com.cput.mediqueuesystem.service;

import java.util.List;

import com.cput.mediqueuesystem.domain.SymptomsAnalysis;

/*
 * ISymptomsAnalysisService.java
 * Service contract for SymptomsAnalysis business logic.
 *
 * Author: Uya
 * Date: 03 August 2026
 */

public interface ISymptomsAnalysisService {

    SymptomsAnalysis create(SymptomsAnalysis symptomsAnalysis);

    SymptomsAnalysis read(String analysisId);

    SymptomsAnalysis update(SymptomsAnalysis symptomsAnalysis);

    void delete(String analysisId);

    List<SymptomsAnalysis> getAll();

    SymptomsAnalysis analyze(String patientId, String inputText);

    List<SymptomsAnalysis> getByPatient(String patientId);
}
