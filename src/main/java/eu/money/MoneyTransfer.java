package eu.money;

import eu.money.configuration.DatabaseManager;
import eu.money.controller.AccountController;
import eu.money.controller.GlobalExceptionHandler;
import eu.money.controller.MoneyTransferController;
import eu.money.repository.AccountRepository;
import eu.money.repository.LockRepository;
import eu.money.repository.impl.AccountRepositoryImpl;
import eu.money.repository.impl.LockRepositoryImpl;
import eu.money.service.AccountService;
import eu.money.service.LockService;
import eu.money.service.MoneyTransferService;
import eu.money.service.impl.AccountServiceImpl;
import eu.money.service.impl.LockServiceImpl;
import eu.money.service.impl.MoneyTransferServiceImpl;
import eu.money.validation.Validator;
import eu.money.validation.impl.AccountValidatorImpl;
import eu.money.validation.impl.TransferValidatorImpl;

import static spark.Spark.port;

public class MoneyTransfer {
    public static void main(String[] args) {
        port(8080);

        DatabaseManager database = new DatabaseManager();

        AccountRepository accountRepository = new AccountRepositoryImpl(database);
        AccountService accountService = new AccountServiceImpl(accountRepository);
        Validator accountValidator = new AccountValidatorImpl();

        LockRepository lockRepository = new LockRepositoryImpl(database);
        LockService lockService = new LockServiceImpl(lockRepository);

        MoneyTransferService moneyTransferService = new MoneyTransferServiceImpl(lockService, accountService, database);
        Validator transferValidator = new TransferValidatorImpl();

        new AccountController(accountService, accountValidator);
        new MoneyTransferController(moneyTransferService, transferValidator);
        new GlobalExceptionHandler();
    }
}
