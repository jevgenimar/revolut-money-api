package eu.money.repository;

import eu.money.model.Account;
import org.sql2o.Connection;

import java.math.BigDecimal;
import java.util.Optional;

public interface AccountRepository {
    Account save(Account account);

    Optional<Account> findById(String publicId);

    void updateBalance(String accountId, BigDecimal balance, Connection connection);

    BigDecimal getBalance(String accountId, Connection connection);
}
