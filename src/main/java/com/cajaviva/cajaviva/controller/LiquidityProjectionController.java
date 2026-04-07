package com.cajaviva.cajaviva.controller;

import com.cajaviva.cajaviva.dao.LiquidityProjectionDao;
import com.cajaviva.cajaviva.entity.LiquidityProjection;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/liquidity-projections")
public class LiquidityProjectionController {

    private final LiquidityProjectionDao liquidityProjectionDao;

    public LiquidityProjectionController(LiquidityProjectionDao liquidityProjectionDao) {
        this.liquidityProjectionDao = liquidityProjectionDao;
    }

    @GetMapping
    public List<LiquidityProjection> getAllProjections() {
        return liquidityProjectionDao.findAll();
    }

    @GetMapping("/account/{account_id}")
    public List<LiquidityProjection> getByAccount(@PathVariable("account_id") UUID account_id) {
        return liquidityProjectionDao.findByAccountId(account_id);
    }

    @PostMapping
    public LiquidityProjection createProjection(@RequestBody LiquidityProjection projection) {
        return liquidityProjectionDao.save(projection);
    }
}
