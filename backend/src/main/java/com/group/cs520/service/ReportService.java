package com.group.cs520.service;
import com.group.cs520.model.Report;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.cs520.repository.ReportRepository;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.lang.reflect.Field;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    public List<Report> allReports() {
        return reportRepository.findAll();
    }

    public Report create(Map<String, Object> reportMap) {
        Report report = new Report(reportMap.get("content").toString());
        reportRepository.insert(report);
        return report;
    }

}
