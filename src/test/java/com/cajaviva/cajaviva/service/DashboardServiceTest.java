package com.cajaviva.cajaviva.service;

import com.cajaviva.cajaviva.controller.dto.DashboardSummaryResponse;
import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.entity.Alert;
import com.cajaviva.cajaviva.entity.Category;
import com.cajaviva.cajaviva.entity.FinancialTransaction;
import com.cajaviva.cajaviva.entity.LiquidityProjection;
import com.cajaviva.cajaviva.repository.JPA.AccountRepository;
import com.cajaviva.cajaviva.repository.JPA.AlertRepository;
import com.cajaviva.cajaviva.repository.JPA.FinancialTransactionRepository;
import com.cajaviva.cajaviva.repository.JPA.LiquidityProjectionRepository;
import com.cajaviva.cajaviva.service.impl.DashboardServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DashboardServiceTest {

    private AccountRepository accountRepository;
    private FinancialTransactionRepository financialTransactionRepository;
    private LiquidityProjectionRepository liquidityProjectionRepository;
    private AlertRepository alertRepository;
    private DashboardServiceImpl service;

    @BeforeEach
    void setUp() {
        accountRepository = mock(AccountRepository.class);
        financialTransactionRepository = mock(FinancialTransactionRepository.class);
        liquidityProjectionRepository = mock(LiquidityProjectionRepository.class);
        alertRepository = mock(AlertRepository.class);
        service = new DashboardServiceImpl(
                accountRepository,
                financialTransactionRepository,
                liquidityProjectionRepository,
                alertRepository
        );
    }

    @Test
    void getSummaryAggregatesUserDashboardData() {
        UUID userId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();

        Account account = new Account();
        account.setId(accountId);
        account.setUserId(userId);
        account.setName("Ahorros");
        account.setAccountType(1);
        account.setBalance(1200);
        account.setCreatedAt(LocalDateTime.now());

        Category incomeCategory = new Category();
        incomeCategory.setType(2);

        FinancialTransaction transaction = new FinancialTransaction();
        transaction.setId(UUID.randomUUID());
        transaction.setDescription("Nomina");
        transaction.setValue(BigDecimal.valueOf(300));
        transaction.setDate(LocalDateTime.now());
        transaction.setAccount(account);
        transaction.setCategory(incomeCategory);

        LiquidityProjection projection = new LiquidityProjection();
        projection.setProjectedBalance(BigDecimal.valueOf(1500));
        projection.setProjectionDate(LocalDate.now().plusDays(5));

        Alert alert = new Alert();
        alert.setId(UUID.randomUUID());
        alert.setMessage("Pago cercano");
        alert.setDate(LocalDate.now().plusDays(2));

        when(accountRepository.findByUserId(userId)).thenReturn(List.of(account));
        when(financialTransactionRepository.findTop5ByAccount_UserIdOrderByDateDesc(userId)).thenReturn(List.of(transaction));
        when(liquidityProjectionRepository.findByAccount_UserIdAndProjectionDateBetweenOrderByProjectionDateAsc(
                userId,
                LocalDate.now(),
                LocalDate.now().plusDays(30)
        )).thenReturn(List.of(projection));
        when(alertRepository.findFirstByLiquidityProjection_Account_UserIdAndStatusAndDateGreaterThanEqualOrderByDateAsc(
                userId,
                1,
                LocalDate.now()
        )).thenReturn(Optional.of(alert));

        DashboardSummaryResponse result = service.getSummary(userId, "ana@example.com");

        assertEquals(userId, result.userId());
        assertEquals("Ana", result.greetingName());
        assertEquals(BigDecimal.valueOf(1200.0), result.totalBalance());
        assertEquals(1, result.accounts().size());
        assertEquals(1, result.recentTransactions().size());
        assertEquals(1, result.liquidity().points().size());
        assertNotNull(result.nextAlert());
        verify(accountRepository).findByUserId(userId);
        verify(financialTransactionRepository).findTop5ByAccount_UserIdOrderByDateDesc(userId);
    }
}
