package com.cajaviva.cajaviva.dto;

import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.entity.LiquidityProjection;
import java.util.List;

public class AccountWithProjectionResponse {
    private Account account;
    private List<LiquidityProjection> projection;
    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }
    public List<LiquidityProjection> getProjection() { return projection; }
    public void setProjection(List<LiquidityProjection> projection) { this.projection = projection; }
}
