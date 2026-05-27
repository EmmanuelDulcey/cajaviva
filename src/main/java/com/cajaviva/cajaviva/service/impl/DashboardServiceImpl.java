package com.cajaviva.cajaviva.service.impl;

import com.cajaviva.cajaviva.controller.dto.DashboardSummaryResponse;
import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.entity.Alert;
import com.cajaviva.cajaviva.entity.FinancialTransaction;
import com.cajaviva.cajaviva.entity.LiquidityProjection;
import com.cajaviva.cajaviva.repository.JPA.AccountRepository;
import com.cajaviva.cajaviva.repository.JPA.AlertRepository;
import com.cajaviva.cajaviva.repository.JPA.FinancialTransactionRepository;
import com.cajaviva.cajaviva.repository.JPA.LiquidityProjectionRepository;
import com.cajaviva.cajaviva.service.DashboardService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class DashboardServiceImpl implements DashboardService {

    private static final int ACTIVE_ALERT_STATUS = 1;
    private static final int INCOME_CATEGORY_TYPE = 2;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("d MMM", new Locale("es", "CO"));

    private final AccountRepository accountRepository;
    private final FinancialTransactionRepository financialTransactionRepository;
    private final LiquidityProjectionRepository liquidityProjectionRepository;
    private final AlertRepository alertRepository;

    public DashboardServiceImpl(
            AccountRepository accountRepository,
            FinancialTransactionRepository financialTransactionRepository,
            LiquidityProjectionRepository liquidityProjectionRepository,
            AlertRepository alertRepository
    ) {
        this.accountRepository = accountRepository;
        this.financialTransactionRepository = financialTransactionRepository;
        this.liquidityProjectionRepository = liquidityProjectionRepository;
        this.alertRepository = alertRepository;
    }

    @Override
    public DashboardSummaryResponse getSummary(UUID userId, String email) {
        List<Account> accounts = accountRepository.findByUserId(userId);
        List<FinancialTransaction> transactions = financialTransactionRepository.findTop5ByAccount_UserIdOrderByDateDesc(userId);
        LocalDate today = LocalDate.now();
        List<LiquidityProjection> projections = liquidityProjectionRepository
                .findByAccount_UserIdAndProjectionDateBetweenOrderByProjectionDateAsc(userId, today, today.plusDays(30));

        BigDecimal totalBalance = accounts.stream()
                .map(account -> BigDecimal.valueOf(account.getBalance()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new DashboardSummaryResponse(
                userId,
                resolveGreetingName(email),
                totalBalance,
                "COP",
                calculateMonthlyVariation(totalBalance, transactions),
                resolveNextAlert(userId, today),
                mapAccounts(accounts),
                mapLiquidity(projections),
                mapTransactions(transactions)
        );
    }

    private String resolveGreetingName(String email) {
        if (email == null || email.isBlank()) {
            return "Usuario";
        }
        String localPart = email.split("@", 2)[0].replace('.', ' ').replace('_', ' ').trim();
        return localPart.isBlank() ? "Usuario" : capitalize(localPart);
    }

    private String capitalize(String value) {
        return value.substring(0, 1).toUpperCase(Locale.ROOT) + value.substring(1);
    }

    private BigDecimal calculateMonthlyVariation(BigDecimal totalBalance, List<FinancialTransaction> transactions) {
        BigDecimal monthlyMovement = transactions.stream()
                .map(transaction -> isPositive(transaction) ? transaction.getValue() : transaction.getValue().negate())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalBalance.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return monthlyMovement
                .multiply(BigDecimal.valueOf(100))
                .divide(totalBalance, 1, RoundingMode.HALF_UP);
    }

    private DashboardSummaryResponse.DashboardAlertResponse resolveNextAlert(UUID userId, LocalDate today) {
        return alertRepository
                .findFirstByLiquidityProjection_Account_UserIdAndStatusAndDateGreaterThanEqualOrderByDateAsc(
                        userId,
                        ACTIVE_ALERT_STATUS,
                        today
                )
                .map(this::mapAlert)
                .orElse(null);
    }

    private DashboardSummaryResponse.DashboardAlertResponse mapAlert(Alert alert) {
        return new DashboardSummaryResponse.DashboardAlertResponse(
                alert.getId(),
                "Alerta financiera",
                alert.getMessage(),
                alert.getDate()
        );
    }

    private List<DashboardSummaryResponse.DashboardAccountResponse> mapAccounts(List<Account> accounts) {
        return accounts.stream()
                .sorted(Comparator.comparing(Account::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(account -> new DashboardSummaryResponse.DashboardAccountResponse(
                        account.getId(),
                        account.getName(),
                        accountTypeLabel(account.getAccountType()),
                        BigDecimal.valueOf(account.getBalance()),
                        account.getAccountType(),
                        null
                ))
                .toList();
    }

    private String accountTypeLabel(int accountType) {
        return switch (accountType) {
            case 1 -> "Cuenta de ahorros";
            case 2 -> "Cuenta corriente";
            case 3 -> "Tarjeta de credito";
            default -> "Cuenta financiera";
        };
    }

    private DashboardSummaryResponse.DashboardLiquidityResponse mapLiquidity(List<LiquidityProjection> projections) {
        List<DashboardSummaryResponse.DashboardLiquidityPointResponse> points = projections.stream()
                .map(projection -> new DashboardSummaryResponse.DashboardLiquidityPointResponse(
                        projection.getProjectionDate(),
                        projection.getProjectedBalance()
                ))
                .toList();

        String message = points.isEmpty()
                ? "Aun no hay proyecciones para los proximos 30 dias."
                : "Flujo proyectado estable para los proximos 30 dias.";

        return new DashboardSummaryResponse.DashboardLiquidityResponse(message, points);
    }

    private List<DashboardSummaryResponse.DashboardTransactionResponse> mapTransactions(List<FinancialTransaction> transactions) {
        return transactions.stream()
                .map(transaction -> new DashboardSummaryResponse.DashboardTransactionResponse(
                        transaction.getId(),
                        transaction.getDescription() == null || transaction.getDescription().isBlank()
                                ? "Movimiento sin descripcion"
                                : transaction.getDescription(),
                        transactionSubtitle(transaction),
                        transaction.getValue(),
                        isPositive(transaction),
                        transaction.getDate()
                ))
                .toList();
    }

    private String transactionSubtitle(FinancialTransaction transaction) {
        String date = transaction.getDate() == null
                ? "Sin fecha"
                : transaction.getDate().toLocalDate().format(DATE_FORMATTER);
        String accountName = transaction.getAccount() == null ? "Cuenta" : transaction.getAccount().getName();
        return date + " · " + accountName;
    }

    private boolean isPositive(FinancialTransaction transaction) {
        return transaction.getCategory() != null
                && transaction.getCategory().getType() != null
                && transaction.getCategory().getType() == INCOME_CATEGORY_TYPE;
    }
}
