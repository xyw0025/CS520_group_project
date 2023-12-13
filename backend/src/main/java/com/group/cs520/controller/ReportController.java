package com.group.cs520.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group.cs520.model.Preference;
import com.group.cs520.model.Report;
import com.group.cs520.service.ReportService;


@RestController
@RequestMapping("/api/v1/report")
public class ReportController {

    @Autowired
    ReportService reportService;

    @GetMapping("/all")
    public ResponseEntity<List<Report>> getAllReports() {
        List<Report> reports = reportService.allReports();
        return ResponseEntity.ok(reports);
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> createReport(@RequestBody Map<String, Object> payload) {
        try {
            Report report = reportService.create(payload);
            return ResponseEntity.ok(report);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", e.getMessage()));
        }
    }
}
