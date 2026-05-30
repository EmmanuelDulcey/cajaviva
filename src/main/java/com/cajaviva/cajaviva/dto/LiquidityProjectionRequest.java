package com.cajaviva.cajaviva.dto;

import java.time.LocalDate;
import java.util.UUID;

public class LiquidityProjectionRequest {
    private UUID accountId;
    private LocalDate startDate;
    private LocalDate endDate;

    public UUID getAccountId() {
        return accountId;
    }
    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }
    public LocalDate getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public LocalDate getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
