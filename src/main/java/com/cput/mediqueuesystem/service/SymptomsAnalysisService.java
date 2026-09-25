package com.cput.mediqueuesystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cput.mediqueuesystem.domain.SymptomsAnalysis;
import com.cput.mediqueuesystem.factory.SymptomsAnalysisFactory;
import com.cput.mediqueuesystem.repository.SymptomsAnalysisRepository;

/*
 * SymptomsAnalysisService.java
 * Implements the business logic for managing symptom analyses.
 *
 * Author: Uya
 * Date: 03 August 2026
 */

@Service
public class SymptomsAnalysisService implements ISymptomsAnalysisService {

    private final SymptomsAnalysisRepository symptomsAnalysisRepository;
    private final SymptomAnalyzerService symptomAnalyzerService;

    @Autowired
    public SymptomsAnalysisService(SymptomsAnalysisRepository symptomsAnalysisRepository,
                                   SymptomAnalyzerService symptomAnalyzerService) {
        this.symptomsAnalysisRepository = symptomsAnalysisRepository;
        this.symptomAnalyzerService = symptomAnalyzerService;
    }

    @Override
    public SymptomsAnalysis create(SymptomsAnalysis symptomsAnalysis) {
        if (symptomsAnalysis == null) {
            return null;
        }
        SymptomsAnalysis validated = SymptomsAnalysisFactory.createSymptomsAnalysis(
                symptomsAnalysis.getAnalysisId(), symptomsAnalysis.getPatient(),
                symptomsAnalysis.getInputText(), symptomsAnalysis.getPredictedConditions(),
                symptomsAnalysis.getSuggestedSymptoms(), symptomsAnalysis.getConfidenceScore(),
                symptomsAnalysis.getCreatedAt());
        if (validated == null) {
            return null;
        }
        return symptomsAnalysisRepository.save(validated);
    }

    @Override
    public SymptomsAnalysis read(String analysisId) {
        return symptomsAnalysisRepository.findById(analysisId).orElse(null);
    }

    @Override
    public SymptomsAnalysis update(SymptomsAnalysis symptomsAnalysis) {
        if (!symptomsAnalysisRepository.existsById(symptomsAnalysis.getAnalysisId())) {
            return null;
        }
        return symptomsAnalysisRepository.save(symptomsAnalysis);
    }

    @Override
    public void delete(String analysisId) {
        symptomsAnalysisRepository.deleteById(analysisId);
    }

    @Override
    public List<SymptomsAnalysis> getAll() {
        return symptomsAnalysisRepository.findAll();
    }

    @Override
    public SymptomsAnalysis analyze(String patientId, String inputText) {
        return symptomAnalyzerService.analyze(patientId, inputText);
    }

    @Override
    public List<SymptomsAnalysis> getByPatient(String patientId) {
        return symptomAnalyzerService.getByPatient(patientId);
    }
}
