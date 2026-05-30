package com.cajaviva.cajaviva.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record DashboardSummaryResponse(
        UUID userId,
        String greetingName,
        BigDecimal totalBalance,
        String currency,
        BigDecimal monthlyBalanceVariationPercent,
        DashboardAlertResponse nextAlert,
        List<DashboardAccountResponse> accounts,
        DashboardLiquidityResponse liquidity,
        List<DashboardTransactionResponse> recentTransactions
) {
    public record DashboardAlertResponse(
            UUID id,
            String title,
            String message,
            LocalDate date
    ) {
    }

    public record DashboardAccountResponse(
            UUID id,
            String name,
            String subtitle,
            BigDecimal balance,
            Integer accountType,
            Integer progressPercentage
    ) {
    }

    public record DashboardLiquidityResponse(
            String message,
            List<DashboardLiquidityPointResponse> points
    ) {
    }

    public record DashboardLiquidityPointResponse(
            LocalDate date,
            BigDecimal projectedBalance
    ) {
    }

    public record DashboardTransactionResponse(
            UUID id,
            String title,
            String subtitle,
            BigDecimal amount,
            boolean positive,
            LocalDateTime date
    ) {
    }
}
