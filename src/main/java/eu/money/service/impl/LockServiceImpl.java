package eu.money.service.impl;

import eu.money.exception.AccountAlreadyLockedException;
import eu.money.model.Lock;
import eu.money.model.LockResourceType;
import eu.money.repository.LockRepository;
import eu.money.service.LockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LockServiceImpl implements LockService {

    private final LockRepository repository;

    private static Logger logger = LoggerFactory.getLogger(LockServiceImpl.class);

    public LockServiceImpl(LockRepository repository) {
        this.repository = repository;
    }

    @Override
    public void runInLock(String accountId, Runnable action) {
        lock(accountId, LockResourceType.ACCOUNT);
        try {
            action.run();
        } finally {
            releaseLock(accountId, LockResourceType.ACCOUNT);
        }
    }

    @Override
    public void runInLock(String sourceAccountId, String targetAccountId, Runnable action) {
        lock(sourceAccountId, LockResourceType.ACCOUNT);
        lock(targetAccountId, LockResourceType.ACCOUNT);
        try {
            action.run();
        } finally {
            releaseLock(sourceAccountId, LockResourceType.ACCOUNT);
            releaseLock(targetAccountId, LockResourceType.ACCOUNT);
        }
    }

    private void lock(String resourceId, LockResourceType lockResourceType) {
        logger.info("Creating lock for resource {}.", resourceId);
        repository.findByResourceIdAndType(resourceId, lockResourceType)
                .ifPresent(s -> {
                    throw new AccountAlreadyLockedException("Account with resourceId " + resourceId + " is already locked");
                });
        repository.save(new Lock(resourceId, lockResourceType));
    }

    private void releaseLock(String resourceId, LockResourceType lockResourceType) {
        logger.info("Releasing lock for resource {}.", resourceId);
        repository.delete(resourceId, lockResourceType);
    }

}
