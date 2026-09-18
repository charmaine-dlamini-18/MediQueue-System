package com.cput.mediqueuesystem.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/*
 * SymptomsAnalysis.java
 * Represents a simplified symptom analysis result for a patient.
 *
 * Author: Uya
 * Date: 03 August 2026
 */

@Entity
@Table(name = "symptoms_analysis")
public class SymptomsAnalysis {

    // Primary Key
    @Id
    @Column(name = "analysis_id")
    private String analysisId;

    // The patient this analysis belongs to
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Lob
    @Column(name = "input_text")
    private String inputText;

    @Lob
    @Column(name = "predicted_conditions")
    private String predictedConditions;

    @Lob
    @Column(name = "suggested_symptoms")
    private String suggestedSymptoms;

    // Confidence score for the analysis
    @Column(name = "confidence_score")
    private double confidenceScore;

    // Date and time the analysis was created
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Default constructor required by JPA
    protected SymptomsAnalysis() {
    }

    // Constructor used by Builder
    private SymptomsAnalysis(Builder builder) {
        this.analysisId = builder.analysisId;
        this.patient = builder.patient;
        this.inputText = builder.inputText;
        this.predictedConditions = builder.predictedConditions;
        this.suggestedSymptoms = builder.suggestedSymptoms;
        this.confidenceScore = builder.confidenceScore;
        this.createdAt = builder.createdAt;
    }

    // Getters

    public String getAnalysisId() {
        return analysisId;
    }

    public Patient getPatient() {
        return patient;
    }

    public String getInputText() {
        return inputText;
    }

    public String getPredictedConditions() {
        return predictedConditions;
    }

    public String getSuggestedSymptoms() {
        return suggestedSymptoms;
    }

    public double getConfidenceScore() {
        return confidenceScore;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Returns the SymptomsAnalysis object as a String
    @Override
    public String toString() {
        return "SymptomsAnalysis{" +
                "analysisId='" + analysisId + '\'' +
                ", patient=" + (patient != null ? patient.getUserId() : null) +
                ", inputText='" + inputText + '\'' +
                ", predictedConditions='" + predictedConditions + '\'' +
                ", suggestedSymptoms='" + suggestedSymptoms + '\'' +
                ", confidenceScore=" + confidenceScore +
                ", createdAt=" + createdAt +
                '}';
    }

    /*
     * Builder class for SymptomsAnalysis.
     */
    public static class Builder {

        private String analysisId;
        private Patient patient;
        private String inputText;
        private String predictedConditions;
        private String suggestedSymptoms;
        private double confidenceScore;
        private LocalDateTime createdAt;

        public Builder setAnalysisId(String analysisId) {
            this.analysisId = analysisId;
            return this;
        }

        public Builder setPatient(Patient patient) {
            this.patient = patient;
            return this;
        }

        public Builder setInputText(String inputText) {
            this.inputText = inputText;
            return this;
        }

        public Builder setPredictedConditions(String predictedConditions) {
            this.predictedConditions = predictedConditions;
            return this;
        }

        public Builder setSuggestedSymptoms(String suggestedSymptoms) {
            this.suggestedSymptoms = suggestedSymptoms;
            return this;
        }

        public Builder setConfidenceScore(double confidenceScore) {
            this.confidenceScore = confidenceScore;
            return this;
        }

        public Builder setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder copy(SymptomsAnalysis symptomsAnalysis) {
            this.analysisId = symptomsAnalysis.analysisId;
            this.patient = symptomsAnalysis.patient;
            this.inputText = symptomsAnalysis.inputText;
            this.predictedConditions = symptomsAnalysis.predictedConditions;
            this.suggestedSymptoms = symptomsAnalysis.suggestedSymptoms;
            this.confidenceScore = symptomsAnalysis.confidenceScore;
            this.createdAt = symptomsAnalysis.createdAt;
            return this;
        }

        public SymptomsAnalysis build() {
            return new SymptomsAnalysis(this);
        }
    }
}
