package eu.money.service;

import eu.money.model.Transfer;

public interface MoneyTransferService {
    void transfer(String sourceAccountId, String targetAccountID, Transfer transfer);

    void withdraw(String sourceAccountId, Transfer transfer);

    void deposit(String sourceAccountId, Transfer transfer);
}
