package com.cajaviva.cajaviva.dao;

import com.cajaviva.cajaviva.entity.LiquidityProjection;

import java.util.List;
import java.util.UUID;

public interface LiquidityProjectionDao extends BaseDao<LiquidityProjection, UUID> {

    List<LiquidityProjection> findByAccountId(UUID account_id);
}
