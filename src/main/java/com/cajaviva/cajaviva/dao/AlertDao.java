package com.cajaviva.cajaviva.dao;

import com.cajaviva.cajaviva.entity.Alert;

import java.util.List;
import java.util.UUID;

public interface AlertDao extends BaseDao<Alert, UUID> {

    List<Alert> findByStatus(Integer status);

    List<Alert> findByType(Integer type);

    List<Alert> findByLiquidityProjectionId(UUID liquidity_projection_id);
}
