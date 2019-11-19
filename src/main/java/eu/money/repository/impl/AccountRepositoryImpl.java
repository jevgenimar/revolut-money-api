package eu.money.repository.impl;

import eu.money.configuration.DatabaseManager;
import eu.money.model.Account;
import eu.money.repository.AccountRepository;
import org.sql2o.Connection;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class AccountRepositoryImpl extends AbstractRepository implements AccountRepository {

    public AccountRepositoryImpl(DatabaseManager databaseManager) {
        super(databaseManager);
    }

    @Override
    public Account save(Account account) {
        try (Connection conn = databaseManager.beginTransaction()) {
            conn.createQuery("insert into account" +
                    "(id, iban, public_id, owner, balance, created_at, modified_at) VALUES (DEFAULT, :iban, :publicId, :owner, :balance, SYSDATE, SYSDATE)")
                    .addParameter("iban", account.getIban())
                    .addParameter("publicId", account.getPublicId())
                    .addParameter("owner", account.getOwner())
                    .addParameter("balance", account.getBalance())
                    .executeUpdate();
            conn.commit();
        }
        return account;
    }

    @Override
    public Optional<Account> findById(String publicId) {
        try (Connection conn = databaseManager.open()) {
            List<Account> accounts = conn.createQuery("select * from account where public_id = :publicId")
                    .addParameter("publicId", publicId)
                    .addColumnMapping("public_id", "publicId")
                    .addColumnMapping("created_at", "createdAt")
                    .addColumnMapping("modified_at", "modifiedAt")
                    .executeAndFetch(Account.class);
            return returnSingleResultOrThrowException(accounts);
        }
    }

    @Override
    public void updateBalance(String accountId, BigDecimal balance, Connection connection) {
        connection.createQuery("update account " +
                "set balance = :balance, " +
                "modified_at = SYSDATE " +
                "where public_id = :publicId ")
                .addParameter("balance", balance)
                .addParameter("publicId", accountId)
                .addColumnMapping("public_id", "publicId")
                .executeUpdate();
    }

    @Override
    public BigDecimal getBalance(String accountId, Connection connection) {
        return connection.createQuery("select balance from account " +
                "where public_id = :publicId ")
                .addParameter("publicId", accountId)
                .executeScalar(BigDecimal.class);
    }
}
