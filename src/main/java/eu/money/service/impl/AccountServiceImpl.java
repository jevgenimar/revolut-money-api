package eu.money.service.impl;

import eu.money.exception.ResourceNotFoundException;
import eu.money.model.Account;
import eu.money.repository.AccountRepository;
import eu.money.service.AccountService;
import eu.money.util.UUIDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;

import java.math.BigDecimal;

public class AccountServiceImpl implements AccountService {

    private static Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    private AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account create(Account account) {
        logger.info("Creating account {}", account.toString());
        account.setPublicId(UUIDGenerator.generateUUID());
        account.setBalance(new BigDecimal(0));
        return accountRepository.save(account);
    }

    @Override
    public Account findByIdOrThrowException(String accountId) {
        logger.info("Retrieving account by id {}", accountId);
        return accountRepository.findById(accountId).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public BigDecimal getBalance(String accountId, Connection connection) {
        logger.info("Getting balance for account {}", accountId);
        return accountRepository.getBalance(accountId, connection);
    }

    @Override
    public void updateBalance(String accountId, BigDecimal newBalance, Connection connection) {
        logger.info("Updating balance for account {}", accountId);
        accountRepository.updateBalance(accountId, newBalance, connection);
    }
}
