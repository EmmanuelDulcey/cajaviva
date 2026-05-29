package com.cajaviva.cajaviva.service.impl;

import com.cajaviva.cajaviva.entity.LiquidityProjection;
import com.cajaviva.cajaviva.repository.JPA.LiquidityProjectionRepository;
import com.cajaviva.cajaviva.entity.Account;
import com.cajaviva.cajaviva.service.LiquidityProjectionService;
import com.cajaviva.cajaviva.exception.ResourceNotFoundException;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class LiquidityProjectionServiceImpl implements LiquidityProjectionService {

    private final LiquidityProjectionRepository liquidityProjectionRepository;
    private final com.cajaviva.cajaviva.repository.JPA.FinancialTransactionRepository financialTransactionRepository;
    private final com.cajaviva.cajaviva.repository.JPA.AccountRepository accountRepository;

    public LiquidityProjectionServiceImpl(LiquidityProjectionRepository liquidityProjectionRepository, com.cajaviva.cajaviva.repository.JPA.FinancialTransactionRepository financialTransactionRepository, com.cajaviva.cajaviva.repository.JPA.AccountRepository accountRepository) {
        this.financialTransactionRepository = financialTransactionRepository;
        this.accountRepository = accountRepository;
        this.liquidityProjectionRepository = liquidityProjectionRepository;
    }

    @Override
    public List<LiquidityProjection> findAll() {
        return liquidityProjectionRepository.findAll();
    }

    @Override
    public LiquidityProjection findById(UUID id) {
        return liquidityProjectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyección de liquidez no encontrada con id: " + id));
    }

    @Override
    public LiquidityProjection create(LiquidityProjection projection) {
        return liquidityProjectionRepository.save(projection);
    }

    @Override
    public LiquidityProjection update(UUID id, LiquidityProjection projection) {
        LiquidityProjection existing = findById(id);
        existing.setAccount(projection.getAccount());
        existing.setProjectionDate(projection.getProjectionDate());
        existing.setAmount(projection.getAmount());
        existing.setUpdatedAt(projection.getUpdatedAt());

        return liquidityProjectionRepository.save(existing);
    }

    @Override
    public void delete(UUID id) {
        liquidityProjectionRepository.deleteById(id);
    }

    @Override
    public List<LiquidityProjection> findByAccount(Account account) {
        return liquidityProjectionRepository.findByAccount(account);
    }

    @Override
    public List<LiquidityProjection> findByProjectionDate(LocalDate projectionDate) {
        return liquidityProjectionRepository.findByProjectionDate(projectionDate);
    }

    @Override
    public List<LiquidityProjection> findByAccountId(UUID account_id) {
        return liquidityProjectionRepository.findByAccount_Id(account_id);
    }

    @Override
    public List<LiquidityProjection> calculateProjection(UUID accountId, LocalDate startDate, LocalDate endDate) {
        // VALIDACIÓN DE FECHAS
        LocalDate today = LocalDate.now();
        if (startDate.isBefore(today)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser en el pasado");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("La fecha final debe ser igual o posterior a la inicial");
        }
        if (startDate.plusYears(1).isBefore(endDate)) {
            throw new IllegalArgumentException("El rango de fechas no puede ser mayor a 1 año");
        }

        // OBTENER TRANSACCIONES POR CUENTA Y RANGO DE FECHAS
        List<com.cajaviva.cajaviva.entity.FinancialTransaction> transactions = financialTransactionRepository.findByAccount_Id(accountId);
        java.util.Map<LocalDate, java.math.BigDecimal> sumByDate = new java.util.HashMap<>();
        transactions.stream()
                .filter(tx -> !tx.getDate().toLocalDate().isBefore(startDate) && !tx.getDate().toLocalDate().isAfter(endDate))
                .forEach(tx -> sumByDate.merge(tx.getDate().toLocalDate(), tx.getValue(), java.math.BigDecimal::add));

        // CALCULAR EL SALDO INICIAL - para simplificar, se toma el balance de la cuenta en base de datos
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new com.cajaviva.cajaviva.exception.ResourceNotFoundException("Cuenta no encontrada"));
        java.math.BigDecimal saldo = java.math.BigDecimal.valueOf(account.getBalance());

        java.util.List<LiquidityProjection> proyecciones = new java.util.ArrayList<>();
        java.time.LocalDate actual = startDate;
        while (!actual.isAfter(endDate)) {
            if (sumByDate.containsKey(actual)) {
                saldo = saldo.add(sumByDate.get(actual));
            }
            LiquidityProjection projection = new LiquidityProjection();
            projection.setAccount(account);
            projection.setProjectionDate(actual);
            projection.setProjectedBalance(saldo);
            projection.setCalculationDate(java.time.LocalDateTime.now());
            projection.setCreatedAt(java.time.LocalDateTime.now());
            projection.setUpdatedAt(java.time.LocalDateTime.now());
            proyecciones.add(projection);
            actual = actual.plusDays(1);
        }
        liquidityProjectionRepository.saveAll(proyecciones);
        return proyecciones;
    }
}
