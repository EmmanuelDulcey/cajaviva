package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.dao.AlertRepository;
import com.cajaviva.cajaviva.entity.Alert;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertRepository alertRepository;

    public AlertController(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @GetMapping
    public List<Alert> getAllAlerts() {
        return alertRepository.findAll();
    }

    @GetMapping("/projection/{projectionId}")
    public List<Alert> getByProjection(@PathVariable UUID projectionId) {
        return alertRepository.findByLiquidityProjectionId(projectionId);
    }

    @PostMapping
    public Alert createAlert(@RequestBody Alert alert) {
        return alertRepository.save(alert);
    }
}
