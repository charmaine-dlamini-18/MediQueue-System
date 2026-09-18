package com.cput.mediqueuesystem.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.cput.mediqueuesystem.domain.Patient;

class SymptomsAnalysisFactoryTest {

    private Patient patient() {
        return new Patient.Builder().setCreatedAt(LocalDateTime.now()).setUserId("P-001").build();
    }

    @Test
    void createSymptomsAnalysis_withValidData_returnsAnalysis() {
        var analysis = SymptomsAnalysisFactory.createSymptomsAnalysis(
                "SA-001", patient(), "Headache and fever", "Flu", "Headache, fever", 0.85, LocalDateTime.now());

        assertNotNull(analysis);
        assertEquals("SA-001", analysis.getAnalysisId());
        assertEquals(0.85, analysis.getConfidenceScore());
    }

    @Test
    void createSymptomsAnalysis_withNullAnalysisId_returnsNull() {
        var analysis = SymptomsAnalysisFactory.createSymptomsAnalysis(
                null, patient(), "Headache", "Flu", "symptoms", 0.85, LocalDateTime.now());

        assertNull(analysis);
    }

    @Test
    void createSymptomsAnalysis_withBlankAnalysisId_returnsNull() {
        var analysis = SymptomsAnalysisFactory.createSymptomsAnalysis(
                "   ", patient(), "Headache", "Flu", "symptoms", 0.85, LocalDateTime.now());

        assertNull(analysis);
    }

    @Test
    void createSymptomsAnalysis_withNullPatient_returnsNull() {
        var analysis = SymptomsAnalysisFactory.createSymptomsAnalysis(
                "SA-002", null, "Headache", "Flu", "symptoms", 0.85, LocalDateTime.now());

        assertNull(analysis);
    }

    @Test
    void createSymptomsAnalysis_withNullInputText_returnsNull() {
        var analysis = SymptomsAnalysisFactory.createSymptomsAnalysis(
                "SA-003", patient(), null, "Flu", "symptoms", 0.85, LocalDateTime.now());

        assertNull(analysis);
    }

    @Test
    void createSymptomsAnalysis_withBlankInputText_returnsNull() {
        var analysis = SymptomsAnalysisFactory.createSymptomsAnalysis(
                "SA-004", patient(), "   ", "Flu", "symptoms", 0.85, LocalDateTime.now());

        assertNull(analysis);
    }

    @Test
    void createSymptomsAnalysis_withNullPredictedConditionsAndCreatedAt_stillReturnsAnalysis() {
        // predictedConditions, suggestedSymptoms, createdAt are optional
        var analysis = SymptomsAnalysisFactory.createSymptomsAnalysis(
                "SA-005", patient(), "Headache", null, null, 0.0, null);

        assertNotNull(analysis);
    }
}