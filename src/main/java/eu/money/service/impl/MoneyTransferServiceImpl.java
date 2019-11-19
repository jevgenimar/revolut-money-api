package eu.money.service.impl;

import eu.money.configuration.DatabaseManager;
import eu.money.exception.NotEnoughBalanceException;
import eu.money.model.Transfer;
import eu.money.service.AccountService;
import eu.money.service.LockService;
import eu.money.service.MoneyTransferService;
import eu.money.util.MoneyOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;

import java.math.BigDecimal;

public class MoneyTransferServiceImpl implements MoneyTransferService {

    private final LockService lockService;
    private final AccountService accountService;
    private final DatabaseManager databaseManager;

    private static Logger logger = LoggerFactory.getLogger(MoneyTransferServiceImpl.class);

    public MoneyTransferServiceImpl(LockService lockService, AccountService accountService, DatabaseManager databaseManager) {
        this.lockService = lockService;
        this.accountService = accountService;
        this.databaseManager = databaseManager;
    }

    @Override
    public void transfer(String sourceAccountId, String targetAccountID, Transfer transfer) {
        logger.info("Entering transfer operation with source account {}, target account {} and transfer {}", sourceAccountId, targetAccountID, transfer);
        accountService.findByIdOrThrowException(sourceAccountId);
        accountService.findByIdOrThrowException(sourceAccountId);

        lockService.runInLock(sourceAccountId, targetAccountID, () -> databaseManager.runInTransaction((connection, o) -> {
            withdraw(sourceAccountId, transfer.getAmount(), connection);
            deposit(targetAccountID, transfer.getAmount(), connection);
        })
        );
    }

    @Override
    public void withdraw(String sourceAccountId, Transfer transfer) {
        logger.info("Entering withdraw operation with source account {} and transfer {}", sourceAccountId, transfer);
        accountService.findByIdOrThrowException(sourceAccountId);
        lockService.runInLock(
                sourceAccountId, () -> databaseManager.runInTransaction((connection, o) -> withdraw(sourceAccountId, transfer.getAmount(), connection))
        );
    }

    @Override
    public void deposit(String sourceAccountId, Transfer transfer) {
        logger.info("Entering deposit operation with source account {} and transfer {}", sourceAccountId, transfer);
        accountService.findByIdOrThrowException(sourceAccountId);
        lockService.runInLock(
                sourceAccountId, () -> databaseManager.runInTransaction((connection, o) -> deposit(sourceAccountId, transfer.getAmount(), connection))
        );
    }

    private void withdraw(String accountId, BigDecimal amount, Connection connection) {
        logger.info("Withdrawing from account {} amount {}", accountId, amount);
        BigDecimal balance = accountService.getBalance(accountId, connection);
        BigDecimal newBalance = MoneyOperation.withdraw(balance, amount);
        if (newBalance.signum() == -1) {
            throw new NotEnoughBalanceException("Not enough balance to withdraw " + amount + " from account " + accountId);
        }
        accountService.updateBalance(accountId, newBalance, connection);
    }

    private void deposit(String accountId, BigDecimal amount, Connection connection) {
        logger.info("Depositing to account {} amount {}", accountId, amount);
        BigDecimal balance = accountService.getBalance(accountId, connection);
        BigDecimal newBalance = MoneyOperation.deposit(balance, amount);
        accountService.updateBalance(accountId, newBalance, connection);
    }


}

