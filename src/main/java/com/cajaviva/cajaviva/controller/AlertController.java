package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.service.AlertService;
import com.cajaviva.cajaviva.entity.Alert;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping
    public List<Alert> getAllAlerts() {
        return alertService.findAll();
    }

    @GetMapping("/projection/{projection_id}")
    public List<Alert> getByProjection(@PathVariable("projection_id") UUID projection_id) {
        return alertService.findByLiquidityProjectionId(projection_id);
    }

    @PostMapping
    public Alert createAlert(@RequestBody Alert alert) {
        return alertService.create(alert);
    }
}
