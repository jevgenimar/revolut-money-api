package eu.money.service;

import eu.money.model.Account;
import org.sql2o.Connection;

import java.math.BigDecimal;

public interface AccountService {
    Account create(Account account);

    Account findByIdOrThrowException(String accountId);

    BigDecimal getBalance(String accountId, Connection connection);

    void updateBalance(String accountId, BigDecimal newBalance, Connection connection);
}
