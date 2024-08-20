package io.github.orionlibs.orion_database_mysql;

import io.github.orionlibs.orion_database.OrionSQLDatabaseAccessObject;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

public class DatabaseDAO implements OrionSQLDatabaseAccessObject
{
    public static String databaseName;
    private DatabaseUpdatesDAO databaseUpdatesDAO;
    private DatabaseQueriesDAO databaseQueriesDAO;
    private DatabaseStaticDAO databaseStaticDAO;
    private DataSource dataSource;
    private JdbcTemplate JDBC;
    private TransactionTemplate transactionalJDBC;


    public void runStaticSQL(String SQLCode)
    {
        databaseStaticDAO.runStaticSQL(SQLCode, getJDBC());
    }


    public int runSQL(String SQLCode)
    {
        return databaseUpdatesDAO.runSQLUpdate(SQLCode, null, getJDBC());
    }


    public int runSQL(String SQLCode, Object[] parameters)
    {
        return databaseUpdatesDAO.runSQLUpdate(SQLCode, parameters, getJDBC());
    }


    public void executeMultipleCommands(String SQLCode)
    {
        databaseUpdatesDAO.executeMultipleCommands(SQLCode, getTransactionalJDBC(), getJDBC());
    }


    public void runDDL(String SQLCode)
    {
        databaseUpdatesDAO.runDDL(SQLCode, getTransactionalJDBC(), getJDBC());
    }


    public int[] runSQLBatch(String SQLCode, List<Object[]> parameters, boolean allowSequentialExecution)
    {
        return databaseUpdatesDAO.runSQLBatchUpdate(SQLCode, parameters, getJDBC(), allowSequentialExecution);
    }


    public int[] runSQLBatch(String SQLCode, boolean allowSequentialExecution)
    {
        return databaseUpdatesDAO.runSQLBatchUpdate(SQLCode, null, getJDBC(), allowSequentialExecution);
    }


    public List<Object> runSQL(String SQLCode, Object modelToUse, Object[] parameters)
    {
        return databaseQueriesDAO.runSQLQuery(SQLCode, modelToUse, parameters, getJDBC());
    }


    public List<Object> runSQL(String SQLCode, Object modelToUse)
    {
        return databaseQueriesDAO.runSQLQuery(SQLCode, modelToUse, null, getJDBC());
    }


    public DataSource getDataSource()
    {
        return dataSource;
    }


    public void setDataSource(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }


    public JdbcTemplate getJDBC()
    {
        return JDBC;
    }


    public void setJDBC(JdbcTemplate jDBC)
    {
        this.JDBC = jDBC;
    }


    public DatabaseUpdatesDAO getDatabaseUpdatesDAO()
    {
        return this.databaseUpdatesDAO;
    }


    public void setDatabaseUpdatesDAO(DatabaseUpdatesDAO databaseUpdatesDAO)
    {
        this.databaseUpdatesDAO = databaseUpdatesDAO;
    }


    public DatabaseQueriesDAO getDatabaseQueriesDAO()
    {
        return this.databaseQueriesDAO;
    }


    public void setDatabaseQueriesDAO(DatabaseQueriesDAO databaseQueriesDAO)
    {
        this.databaseQueriesDAO = databaseQueriesDAO;
    }


    public DatabaseStaticDAO getDatabaseStaticDAO()
    {
        return this.databaseStaticDAO;
    }


    public void setDatabaseStaticDAO(DatabaseStaticDAO databaseStaticDAO)
    {
        this.databaseStaticDAO = databaseStaticDAO;
    }


    public TransactionTemplate getTransactionalJDBC()
    {
        return this.transactionalJDBC;
    }


    public void setTransactionalJDBC(TransactionTemplate transactionalJDBC)
    {
        this.transactionalJDBC = transactionalJDBC;
    }
}