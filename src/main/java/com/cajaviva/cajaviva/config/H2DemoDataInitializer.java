package com.cajaviva.cajaviva.config;

import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.entity.AuthUser;
import com.cajaviva.cajaviva.entity.Category;
import com.cajaviva.cajaviva.entity.FinancialTransaction;
import com.cajaviva.cajaviva.entity.LiquidityProjection;
import com.cajaviva.cajaviva.repository.JPA.AccountRepository;
import com.cajaviva.cajaviva.repository.JPA.AuthUserRepository;
import com.cajaviva.cajaviva.repository.JPA.CategoryRepository;
import com.cajaviva.cajaviva.repository.JPA.FinancialTransactionRepository;
import com.cajaviva.cajaviva.repository.JPA.LiquidityProjectionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Configuration
@Profile("h2")
public class H2DemoDataInitializer {

    public static final String DEMO_EMAIL = "demo@cajaviva.test";
    public static final String DEMO_PASSWORD = "Demo12345!";

    @Bean
    CommandLineRunner seedH2DemoData(
            AuthUserRepository authUserRepository,
            AccountRepository accountRepository,
            CategoryRepository categoryRepository,
            FinancialTransactionRepository financialTransactionRepository,
            LiquidityProjectionRepository liquidityProjectionRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            if (authUserRepository.findByEmailIgnoreCase(DEMO_EMAIL).isPresent()) {
                return;
            }

            LocalDateTime now = LocalDateTime.now();
            AuthUser user = new AuthUser();
            user.setId(UUID.randomUUID());
            user.setName("Usuario Demo");
            user.setEmail(DEMO_EMAIL);
            user.setPasswordDigest(passwordEncoder.encode(DEMO_PASSWORD));
            user.setActive(true);
            user.setCreatedAt(now);
            user.setUpdatedAt(now);
            authUserRepository.save(user);

            Account savings = createAccount(user.getId(), "Ahorros Principal", 1, 2450000.0, now.minusDays(10));
            Account checking = createAccount(user.getId(), "Cuenta Diaria", 2, 680000.0, now.minusDays(4));
            accountRepository.saveAll(List.of(savings, checking));

            Category salary = createCategory("Salario", 2, "Ingresos laborales", now);
            Category food = createCategory("Alimentacion", 1, "Gastos de comida y mercado", now);
            Category transport = createCategory("Transporte", 1, "Movilidad diaria", now);
            categoryRepository.saveAll(List.of(salary, food, transport));

            financialTransactionRepository.saveAll(List.of(
                    createTransaction(savings, salary, "Nomina mensual", "2800000", now.minusDays(2)),
                    createTransaction(checking, food, "Mercado semanal", "185000", now.minusDays(1)),
                    createTransaction(checking, transport, "Recarga transporte", "62000", now.minusHours(8))
            ));

            liquidityProjectionRepository.saveAll(List.of(
                    createProjection(savings, "3130000", LocalDate.now().plusDays(7), now),
                    createProjection(savings, "2980000", LocalDate.now().plusDays(14), now),
                    createProjection(savings, "3260000", LocalDate.now().plusDays(21), now),
                    createProjection(savings, "3410000", LocalDate.now().plusDays(30), now)
            ));
        };
    }

    private Account createAccount(UUID userId, String name, int accountType, double balance, LocalDateTime createdAt) {
        Account account = new Account();
        account.setId(UUID.randomUUID());
        account.setUserId(userId);
        account.setName(name);
        account.setAccountType(accountType);
        account.setBalance(balance);
        account.setCreatedAt(createdAt);
        account.setUpdatedAt(LocalDateTime.now());
        return account;
    }

    private Category createCategory(String name, int type, String description, LocalDateTime now) {
        Category category = new Category();
        category.setName(name);
        category.setType(type);
        category.setDescription(description);
        category.setCreatedAt(now);
        category.setUpdatedAt(now);
        return category;
    }

    private FinancialTransaction createTransaction(
            Account account,
            Category category,
            String description,
            String value,
            LocalDateTime date
    ) {
        FinancialTransaction transaction = new FinancialTransaction();
        transaction.setAccount(account);
        transaction.setCategory(category);
        transaction.setDescription(description);
        transaction.setValue(new BigDecimal(value));
        transaction.setDate(date);
        transaction.setStatus(1);
        transaction.setCreatedAt(date);
        transaction.setUpdatedAt(LocalDateTime.now());
        return transaction;
    }

    private LiquidityProjection createProjection(
            Account account,
            String projectedBalance,
            LocalDate projectionDate,
            LocalDateTime now
    ) {
        LiquidityProjection projection = new LiquidityProjection();
        projection.setAccount(account);
        projection.setProjectedBalance(new BigDecimal(projectedBalance));
        projection.setProjectionDate(projectionDate);
        projection.setCalculationDate(now);
        projection.setCreatedAt(now);
        projection.setUpdatedAt(now);
        return projection;
    }
}
