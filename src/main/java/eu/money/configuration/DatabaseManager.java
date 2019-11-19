package eu.money.configuration;

import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.StatementRunnable;

public class DatabaseManager {

    private final static Sql2o SQL2O = new Sql2o("jdbc:hsqldb:mem:testDB", "sa", "");

    public DatabaseManager() {
        DatabaseSchemaInitializer.initializeDatabase(SQL2O);
    }

    public Connection beginTransaction() {
        return SQL2O.beginTransaction();
    }

    public Connection open() {
        return SQL2O.open();
    }

    public void runInTransaction(StatementRunnable runnable) {
        SQL2O.runInTransaction(runnable);
    }
}
