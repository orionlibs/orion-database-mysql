package io.github.orionlibs.orion_database_mysql;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@Transactional("PlatformTransactionManager")
public class DatabaseStaticDAO
{
    public void runStaticSQL(String SQLCode, JdbcTemplate JDBC)
    {
        try
        {
            JDBC.execute(SQLCode);
        }
        catch(DataAccessException e)
        {
            logErrorToConsole(e.getMessage());
            throw e;
        }
    }
}