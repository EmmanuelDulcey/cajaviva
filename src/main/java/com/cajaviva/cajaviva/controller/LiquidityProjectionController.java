package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.service.LiquidityProjectionService;
import com.cajaviva.cajaviva.entity.LiquidityProjection;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/liquidity-projections")
public class LiquidityProjectionController {

    private final LiquidityProjectionService liquidityProjectionService;

    public LiquidityProjectionController(LiquidityProjectionService liquidityProjectionService) {
        this.liquidityProjectionService = liquidityProjectionService;
    }

    @GetMapping
    public List<LiquidityProjection> getAllProjections() {
        return liquidityProjectionService.findAll();
    }

    @GetMapping("/account/{account_id}")
    public List<LiquidityProjection> getByAccount(@PathVariable("account_id") UUID account_id) {
        return liquidityProjectionService.findByAccountId(account_id);
    }

    @PostMapping
    public LiquidityProjection createProjection(@RequestBody LiquidityProjection projection) {
        return liquidityProjectionService.create(projection);
    }
}
