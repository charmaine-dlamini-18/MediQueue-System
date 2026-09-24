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
 * VitalSigns.java
 * Represents recorded vital signs for a visit.
 *
 * Author: Uya
 * Date: 03 August 2026
 */

@Entity
@Table(name = "vital_signs")
public class VitalSigns {

    // Primary Key
    @Id
    @Column(name = "vital_id")
    private String vitalId;

    // The visit these vital signs belong to
    @ManyToOne
    @JoinColumn(name = "visit_id", nullable = false)
    private Visit visit;

    // Temperature reading
    @Column(name = "temperature")
    private String temperature;

    // Blood pressure reading
    @Column(name = "blood_pressure")
    private String bloodPressure;

    // Heart rate reading
    @Column(name = "heart_rate")
    private String heartRate;

    // Weight recording
    @Column(name = "weight")
    private String weight;

    // Oxygen saturation reading, e.g. "98"
    @Column(name = "oxygen_saturation")
    private String oxygenSaturation;

    // Respiratory rate reading, e.g. "16"
    @Column(name = "respiratory_rate")
    private String respiratoryRate;

    // Patient-reported symptoms
    @Lob
    @Column(name = "symptoms")
    private String symptoms;

    // Nurse observations / notes
    @Lob
    @Column(name = "nurse_notes")
    private String nurseNotes;

    // Staff member who recorded these vitals
    @ManyToOne
    @JoinColumn(name = "recorded_by")
    private Staff recordedBy;

    // Date and time the vitals were recorded
    @Column(name = "recorded_at")
    private LocalDateTime recordedAt;

    // Default constructor required by JPA
    protected VitalSigns() {
    }

    // Constructor used by Builder
    private VitalSigns(Builder builder) {
        this.vitalId = builder.vitalId;
        this.visit = builder.visit;
        this.temperature = builder.temperature;
        this.bloodPressure = builder.bloodPressure;
        this.heartRate = builder.heartRate;
        this.weight = builder.weight;
        this.oxygenSaturation = builder.oxygenSaturation;
        this.respiratoryRate = builder.respiratoryRate;
        this.symptoms = builder.symptoms;
        this.nurseNotes = builder.nurseNotes;
        this.recordedBy = builder.recordedBy;
        this.recordedAt = builder.recordedAt;
    }

    // Getters

    public String getVitalId() {
        return vitalId;
    }

    public Visit getVisit() {
        return visit;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public String getHeartRate() {
        return heartRate;
    }

    public String getWeight() {
        return weight;
    }

    public String getOxygenSaturation() {
        return oxygenSaturation;
    }

    public String getRespiratoryRate() {
        return respiratoryRate;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public String getNurseNotes() {
        return nurseNotes;
    }

    public Staff getRecordedBy() {
        return recordedBy;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    // Returns the VitalSigns object as a String
    @Override
    public String toString() {
        return "VitalSigns{" +
                "vitalId='" + vitalId + '\'' +
                ", visit=" + (visit != null ? visit.getVisitId() : null) +
                ", temperature='" + temperature + '\'' +
                ", bloodPressure='" + bloodPressure + '\'' +
                ", heartRate='" + heartRate + '\'' +
                ", weight='" + weight + '\'' +
                ", oxygenSaturation='" + oxygenSaturation + '\'' +
                ", respiratoryRate='" + respiratoryRate + '\'' +
                ", symptoms='" + symptoms + '\'' +
                ", nurseNotes='" + nurseNotes + '\'' +
                ", recordedBy=" + (recordedBy != null ? recordedBy.getUserId() : null) +
                ", recordedAt=" + recordedAt +
                '}';
    }

    /*
     * Builder class for VitalSigns.
     */
    public static class Builder {

        private String vitalId;
        private Visit visit;
        private String temperature;
        private String bloodPressure;
        private String heartRate;
        private String weight;
        private String oxygenSaturation;
        private String respiratoryRate;
        private String symptoms;
        private String nurseNotes;
        private Staff recordedBy;
        private LocalDateTime recordedAt;

        public Builder setVitalId(String vitalId) {
            this.vitalId = vitalId;
            return this;
        }

        public Builder setVisit(Visit visit) {
            this.visit = visit;
            return this;
        }

        public Builder setTemperature(String temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder setBloodPressure(String bloodPressure) {
            this.bloodPressure = bloodPressure;
            return this;
        }

        public Builder setHeartRate(String heartRate) {
            this.heartRate = heartRate;
            return this;
        }

        public Builder setWeight(String weight) {
            this.weight = weight;
            return this;
        }

        public Builder setOxygenSaturation(String oxygenSaturation) {
            this.oxygenSaturation = oxygenSaturation;
            return this;
        }

        public Builder setRespiratoryRate(String respiratoryRate) {
            this.respiratoryRate = respiratoryRate;
            return this;
        }

        public Builder setSymptoms(String symptoms) {
            this.symptoms = symptoms;
            return this;
        }

        public Builder setNurseNotes(String nurseNotes) {
            this.nurseNotes = nurseNotes;
            return this;
        }

        public Builder setRecordedBy(Staff recordedBy) {
            this.recordedBy = recordedBy;
            return this;
        }

        public Builder setRecordedAt(LocalDateTime recordedAt) {
            this.recordedAt = recordedAt;
            return this;
        }

        public Builder copy(VitalSigns vitalSigns) {
            this.vitalId = vitalSigns.vitalId;
            this.visit = vitalSigns.visit;
            this.temperature = vitalSigns.temperature;
            this.bloodPressure = vitalSigns.bloodPressure;
            this.heartRate = vitalSigns.heartRate;
            this.weight = vitalSigns.weight;
            this.oxygenSaturation = vitalSigns.oxygenSaturation;
            this.respiratoryRate = vitalSigns.respiratoryRate;
            this.symptoms = vitalSigns.symptoms;
            this.nurseNotes = vitalSigns.nurseNotes;
            this.recordedBy = vitalSigns.recordedBy;
            this.recordedAt = vitalSigns.recordedAt;
            return this;
        }

        public VitalSigns build() {
            return new VitalSigns(this);
        }
    }
}
