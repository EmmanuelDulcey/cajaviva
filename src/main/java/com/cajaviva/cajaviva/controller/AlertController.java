package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.dao.AlertDao;
import com.cajaviva.cajaviva.entity.Alert;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertDao alertDao;

    public AlertController(AlertDao alertDao) {
        this.alertDao = alertDao;
    }

    @GetMapping
    public List<Alert> getAllAlerts() {
        return alertDao.findAll();
    }

    @GetMapping("/projection/{projection_id}")
    public List<Alert> getByProjection(@PathVariable("projection_id") UUID projection_id) {
        return alertDao.findByLiquidityProjectionId(projection_id);
    }

    @PostMapping
    public Alert createAlert(@RequestBody Alert alert) {
        return alertDao.save(alert);
    }
}
