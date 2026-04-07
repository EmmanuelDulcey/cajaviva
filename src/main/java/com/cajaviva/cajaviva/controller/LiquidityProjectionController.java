package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.dao.LiquidityProjectionRepository;
import com.cajaviva.cajaviva.entity.LiquidityProjection;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/liquidity-projections")
public class LiquidityProjectionController {

    private final LiquidityProjectionRepository liquidityProjectionRepository;

    public LiquidityProjectionController(LiquidityProjectionRepository liquidityProjectionRepository) {
        this.liquidityProjectionRepository = liquidityProjectionRepository;
    }

    @GetMapping
    public List<LiquidityProjection> getAllProjections() {
        return liquidityProjectionRepository.findAll();
    }

    @GetMapping("/account/{accountId}")
    public List<LiquidityProjection> getByAccount(@PathVariable UUID accountId) {
        return liquidityProjectionRepository.findByAccountId(accountId);
    }

    @PostMapping
    public LiquidityProjection createProjection(@RequestBody LiquidityProjection projection) {
        return liquidityProjectionRepository.save(projection);
    }
}
