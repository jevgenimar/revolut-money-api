package eu.money.util;

import java.math.BigDecimal;

public final class MoneyOperation {

    public static BigDecimal withdraw(BigDecimal balance, BigDecimal amount) {
        return balance.subtract(amount);
    }

    public static BigDecimal deposit(BigDecimal balance, BigDecimal amount) {
        return balance.add(amount);
    }
}
