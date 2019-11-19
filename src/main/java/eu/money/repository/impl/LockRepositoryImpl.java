package eu.money.repository.impl;

import eu.money.configuration.DatabaseManager;
import eu.money.model.Lock;
import eu.money.model.LockResourceType;
import eu.money.repository.LockRepository;
import org.sql2o.Connection;

import java.util.List;
import java.util.Optional;

public class LockRepositoryImpl extends AbstractRepository implements LockRepository {

    public LockRepositoryImpl(DatabaseManager databaseManager) {
        super(databaseManager);
    }

    @Override
    public Lock save(Lock lock) {
        try (Connection conn = databaseManager.beginTransaction()) {
            conn.createQuery("insert into lock" +
                    "(id, resource_id, type, created_at) VALUES (DEFAULT, :resourceId, :type, SYSDATE)")
                    .addParameter("resourceId", lock.getResourceId())
                    .addParameter("type", lock.getType().name())
                    .addColumnMapping("resource_id", "resourceId")
                    .executeUpdate();
            conn.commit();
        }
        return lock;
    }

    @Override
    public Optional<Lock> findByResourceIdAndType(String resourceId, LockResourceType type) {
        try (Connection conn = databaseManager.beginTransaction()) {
            List<Lock> locks =  conn.createQuery("select * from lock where " +
                    "resource_id = :resourceId " +
                    "and type = :type")
                    .addParameter("resourceId", resourceId)
                    .addParameter("type", type.name())
                    .addColumnMapping("resource_id", "resourceId")
                    .addColumnMapping("created_at", "createdAt")
                    .executeAndFetch(Lock.class);
            return returnSingleResultOrThrowException(locks);
        }
    }

    @Override
    public void delete(String resourceId, LockResourceType type) {
        try (Connection conn = databaseManager.beginTransaction()) {
            conn.createQuery("delete from lock where " +
                    "resource_id = :resourceId " +
                    "and type = :type")
                    .addParameter("resourceId", resourceId)
                    .addParameter("type", type)
                    .executeUpdate();
            conn.commit();
        }
    }
}
