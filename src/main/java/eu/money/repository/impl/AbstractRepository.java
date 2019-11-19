package eu.money.repository.impl;

import eu.money.configuration.DatabaseManager;
import eu.money.exception.NonUniqueResultException;

import java.util.List;
import java.util.Optional;

public abstract class AbstractRepository {

    protected DatabaseManager databaseManager;

    public AbstractRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    protected <T> Optional returnSingleResultOrThrowException(List<T> list) {
        if (list.size() > 1) {
            throw new NonUniqueResultException("Query returned more then one result.");
        }
        if (list.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(list.get(0));
    }
}
