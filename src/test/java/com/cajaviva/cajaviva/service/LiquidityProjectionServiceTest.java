package com.cajaviva.cajaviva.service;

import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.entity.FinancialTransaction;
import com.cajaviva.cajaviva.entity.LiquidityProjection;
import com.cajaviva.cajaviva.exception.ResourceNotFoundException;
import com.cajaviva.cajaviva.repository.JPA.AccountRepository;
import com.cajaviva.cajaviva.repository.JPA.FinancialTransactionRepository;
import com.cajaviva.cajaviva.repository.JPA.LiquidityProjectionRepository;
import com.cajaviva.cajaviva.service.impl.LiquidityProjectionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LiquidityProjectionServiceTest {
    private LiquidityProjectionRepository liquidityProjectionRepository;
    private FinancialTransactionRepository financialTransactionRepository;
    private AccountRepository accountRepository;
    private LiquidityProjectionServiceImpl liquidityProjectionService;
    private Account account;
    private UUID accountId;

    @BeforeEach
    void setUp() {
        liquidityProjectionRepository = mock(LiquidityProjectionRepository.class);
        financialTransactionRepository = mock(FinancialTransactionRepository.class);
        accountRepository = mock(AccountRepository.class);
        liquidityProjectionService = new LiquidityProjectionServiceImpl(
                liquidityProjectionRepository, financialTransactionRepository, accountRepository);

        accountId = UUID.randomUUID();
        account = new Account();
        account.setId(accountId);
        account.setBalance(1000.0);
    }

    @Test
    void testCalculateProjection_ValidRangeWithTransactions() {
        LocalDate today = LocalDate.now();
        LocalDate start = today.plusDays(1);
        LocalDate end = start.plusDays(2);
        Account acc = account;
        List<FinancialTransaction> txs = Arrays.asList(
                fakeTx(acc, new BigDecimal("100"), start.atStartOfDay()),
                fakeTx(acc, new BigDecimal("-50"), start.plusDays(1).atStartOfDay())
        );
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(financialTransactionRepository.findByAccount_Id(accountId)).thenReturn(txs);
        ArgumentCaptor<List<LiquidityProjection>> captor = ArgumentCaptor.forClass(List.class);
        when(liquidityProjectionRepository.saveAll(captor.capture())).thenReturn(null);
        List<LiquidityProjection> res = liquidityProjectionService.calculateProjection(accountId, start, end);
        assertEquals(3, res.size());
        assertEquals(0, new BigDecimal("1100.0").compareTo(res.get(0).getProjectedBalance()));
        assertEquals(0, new BigDecimal("1050.0").compareTo(res.get(1).getProjectedBalance()));
        assertEquals(0, new BigDecimal("1050.0").compareTo(res.get(2).getProjectedBalance()));
    }

    @Test
    void testCalculateProjection_ValidRangeNoTransactions() {
        LocalDate today = LocalDate.now();
        LocalDate start = today.plusDays(1);
        LocalDate end = start.plusDays(1);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(financialTransactionRepository.findByAccount_Id(accountId)).thenReturn(Collections.emptyList());
        ArgumentCaptor<List<LiquidityProjection>> captor = ArgumentCaptor.forClass(List.class);
        when(liquidityProjectionRepository.saveAll(captor.capture())).thenReturn(null);
        List<LiquidityProjection> res = liquidityProjectionService.calculateProjection(accountId, start, end);
        assertEquals(2, res.size());
        assertEquals(0, new BigDecimal("1000.0").compareTo(res.get(0).getProjectedBalance()));
        assertEquals(0, new BigDecimal("1000.0").compareTo(res.get(1).getProjectedBalance()));
    }

    @Test
    void testCalculateProjection_MultipleTransactionsSameDay() {
        LocalDate today = LocalDate.now();
        LocalDate start = today.plusDays(1);
        LocalDate end = start;
        List<FinancialTransaction> txs = Arrays.asList(
                fakeTx(account, new BigDecimal("200.00"), start.atStartOfDay()),
                fakeTx(account, new BigDecimal("50.00"), start.atStartOfDay()),
                fakeTx(account, new BigDecimal("-30.00"), start.atStartOfDay())
        );
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(financialTransactionRepository.findByAccount_Id(accountId)).thenReturn(txs);
        List<LiquidityProjection> res = liquidityProjectionService.calculateProjection(accountId, start, end);
        assertEquals(1, res.size());
        assertEquals(0, new BigDecimal("1220.00").compareTo(res.get(0).getProjectedBalance()));
    }

    @Test
    void testCalculateProjection_TransactionsOutsideRangeIgnored() {
        LocalDate today = LocalDate.now();
        LocalDate start = today.plusDays(5);
        LocalDate end = start.plusDays(2);
        List<FinancialTransaction> txs = Arrays.asList(
                fakeTx(account, new BigDecimal("500"), today.minusDays(1).atStartOfDay()),
                fakeTx(account, new BigDecimal("9999"), today.plusDays(10).atStartOfDay())
        );
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(financialTransactionRepository.findByAccount_Id(accountId)).thenReturn(txs);
        List<LiquidityProjection> res = liquidityProjectionService.calculateProjection(accountId, start, end);
        assertEquals(3, res.size());
        for (LiquidityProjection p : res) {
            assertEquals(0, new BigDecimal("1000.0").compareTo(p.getProjectedBalance()));
        }
    }

    @Test
    void testCalculateProjection_RangeExactlyOneYearIsValid() {
        LocalDate today = LocalDate.now();
        LocalDate start = today.plusDays(1);
        LocalDate end = start.plusYears(1);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(financialTransactionRepository.findByAccount_Id(accountId)).thenReturn(Collections.emptyList());
        List<LiquidityProjection> res = liquidityProjectionService.calculateProjection(accountId, start, end);
        assertEquals(366, res.size());
    }

    @Test
    void testCalculateProjection_RangeGreaterThanOneYearThrowsException() {
        LocalDate today = LocalDate.now();
        LocalDate start = today.plusDays(1);
        LocalDate end = start.plusYears(1).plusDays(1);
        assertThrows(IllegalArgumentException.class,
                () -> liquidityProjectionService.calculateProjection(accountId, start, end));
    }

    @Test
    void testCalculateProjection_AccountNotFoundThrowsException() {
        LocalDate today = LocalDate.now();
        LocalDate start = today.plusDays(1);
        LocalDate end = start.plusDays(1);
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> liquidityProjectionService.calculateProjection(accountId, start, end));
    }

    @Test
    void testCalculateProjection_InvalidDateThrowsException() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        assertThrows(IllegalArgumentException.class,
                () -> liquidityProjectionService.calculateProjection(accountId, yesterday, today));
    }

    @Test
    void testCalculateProjection_EndBeforeStartThrowsException() {
        LocalDate today = LocalDate.now();
        LocalDate future = today.plusDays(5);
        assertThrows(IllegalArgumentException.class,
                () -> liquidityProjectionService.calculateProjection(accountId, future, today));
    }

    @Test
    void testCalculateProjection_SingleDayRange() {
        LocalDate today = LocalDate.now();
        LocalDate start = today.plusDays(1);
        LocalDate end = start;
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(financialTransactionRepository.findByAccount_Id(accountId)).thenReturn(Collections.emptyList());
        List<LiquidityProjection> res = liquidityProjectionService.calculateProjection(accountId, start, end);
        assertEquals(1, res.size());
        assertEquals(0, new BigDecimal("1000.0").compareTo(res.get(0).getProjectedBalance()));
    }

    @Test
    void testCalculateProjection_PrecisionWithDecimalValues() {
        LocalDate today = LocalDate.now();
        LocalDate start = today.plusDays(1);
        LocalDate end = start.plusDays(2);
        account.setBalance(0.0);
        List<FinancialTransaction> txs = Arrays.asList(
                fakeTx(account, new BigDecimal("1234.56"), start.atStartOfDay()),
                fakeTx(account, new BigDecimal("0.01"), start.plusDays(1).atStartOfDay()),
                fakeTx(account, new BigDecimal("-0.99"), start.plusDays(2).atStartOfDay())
        );
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(financialTransactionRepository.findByAccount_Id(accountId)).thenReturn(txs);
        List<LiquidityProjection> res = liquidityProjectionService.calculateProjection(accountId, start, end);
        assertEquals(3, res.size());
        assertEquals(0, new BigDecimal("1234.56").compareTo(res.get(0).getProjectedBalance()),
                "Saldo día 1 debe ser exactamente 1234.56");
        assertEquals(0, new BigDecimal("1234.57").compareTo(res.get(1).getProjectedBalance()),
                "Saldo día 2 debe ser exactamente 1234.57");
        assertEquals(0, new BigDecimal("1233.58").compareTo(res.get(2).getProjectedBalance()),
                "Saldo día 3 debe ser exactamente 1233.58");
    }

    @Test
    void testCalculateProjection_NinetyPercentPrecisionAcrossMultipleDays() {
        LocalDate today = LocalDate.now();
        LocalDate start = today.plusDays(1);
        LocalDate end = start.plusDays(9);
        account.setBalance(5000.0);
        List<FinancialTransaction> txs = new ArrayList<>();
        BigDecimal totalNetChange = BigDecimal.ZERO;
        for (int i = 0; i < 10; i++) {
            BigDecimal val = new BigDecimal(String.format(Locale.US, "%.2f", 100.0 + i * 10.5));
            txs.add(fakeTx(account, val, start.plusDays(i).atStartOfDay()));
            totalNetChange = totalNetChange.add(val);
        }
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(financialTransactionRepository.findByAccount_Id(accountId)).thenReturn(txs);
        List<LiquidityProjection> res = liquidityProjectionService.calculateProjection(accountId, start, end);
        BigDecimal expectedFinal = BigDecimal.valueOf(5000.0).add(totalNetChange);
        BigDecimal actualFinal = res.get(res.size() - 1).getProjectedBalance();
        BigDecimal diff = expectedFinal.subtract(actualFinal).abs();
        BigDecimal maxError = expectedFinal.multiply(new BigDecimal("0.10"));
        assertTrue(diff.compareTo(maxError) <= 0,
                "Error " + diff + " supera el 10%% del valor esperado " + expectedFinal);
    }

    @Test
    void calculateProjectionSavesAllProjections() {
        LocalDate today = LocalDate.now();
        LocalDate start = today.plusDays(1);
        LocalDate end = start.plusDays(2);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(financialTransactionRepository.findByAccount_Id(accountId)).thenReturn(Collections.emptyList());
        liquidityProjectionService.calculateProjection(accountId, start, end);
        verify(liquidityProjectionRepository, times(1)).saveAll(anyList());
    }

    static FinancialTransaction fakeTx(Account account, BigDecimal val, LocalDateTime date) {
        FinancialTransaction tx = new FinancialTransaction();
        tx.setId(UUID.randomUUID());
        tx.setAccount(account);
        tx.setValue(val);
        tx.setDate(date);
        tx.setStatus(1);
        return tx;
    }
}
