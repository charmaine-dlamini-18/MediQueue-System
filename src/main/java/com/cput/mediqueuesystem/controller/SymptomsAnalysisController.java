package com.cput.mediqueuesystem.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cput.mediqueuesystem.domain.SymptomsAnalysis;
import com.cput.mediqueuesystem.service.ISymptomsAnalysisService;

/*
 * SymptomsAnalysisController.java
 * REST controller for managing symptom analyses.
 *
 * Author: Uya
 * Date: 03 August 2026
 */

@RestController
@RequestMapping("/mediqueue/symptoms-analysis")
@CrossOrigin(origins = "*")
public class SymptomsAnalysisController {

    private final ISymptomsAnalysisService symptomsAnalysisService;

    @Autowired
    public SymptomsAnalysisController(ISymptomsAnalysisService symptomsAnalysisService) {
        this.symptomsAnalysisService = symptomsAnalysisService;
    }

    @PostMapping("/create")
    public ResponseEntity<SymptomsAnalysis> create(@RequestBody SymptomsAnalysis symptomsAnalysis) {
        SymptomsAnalysis created = symptomsAnalysisService.create(symptomsAnalysis);
        if (created == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<SymptomsAnalysis> read(@PathVariable("id") String analysisId) {
        SymptomsAnalysis symptomsAnalysis = symptomsAnalysisService.read(analysisId);
        if (symptomsAnalysis == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(symptomsAnalysis, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<SymptomsAnalysis> update(@RequestBody SymptomsAnalysis symptomsAnalysis) {
        SymptomsAnalysis updated = symptomsAnalysisService.update(symptomsAnalysis);
        if (updated == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String analysisId) {
        symptomsAnalysisService.delete(analysisId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/all")
    public ResponseEntity<List<SymptomsAnalysis>> getAll() {
        return new ResponseEntity<>(symptomsAnalysisService.getAll(), HttpStatus.OK);
    }

    @PostMapping("/analyze")
    public ResponseEntity<SymptomsAnalysis> analyze(@RequestBody Map<String, String> request) {
        String patientId = request.get("patientId");
        String inputText = request.get("inputText");
        SymptomsAnalysis created = symptomsAnalysisService.analyze(patientId, inputText);
        if (created == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<SymptomsAnalysis>> getByPatient(@PathVariable("patientId") String patientId) {
        return new ResponseEntity<>(symptomsAnalysisService.getByPatient(patientId), HttpStatus.OK);
    }
}
