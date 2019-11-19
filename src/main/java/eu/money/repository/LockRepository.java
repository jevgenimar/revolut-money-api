package eu.money.repository;

import eu.money.model.Lock;
import eu.money.model.LockResourceType;

import java.util.Optional;

public interface LockRepository {
    Lock save(Lock lock);

    Optional<Lock> findByResourceIdAndType(String resourceId, LockResourceType type);

    void delete(String resourceId, LockResourceType type);
}
