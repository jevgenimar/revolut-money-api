package eu.money.service;

public interface LockService {
    void runInLock(String accountId, Runnable action);

    void runInLock(String sourceAccountId, String targetAccountId, Runnable action);
}
