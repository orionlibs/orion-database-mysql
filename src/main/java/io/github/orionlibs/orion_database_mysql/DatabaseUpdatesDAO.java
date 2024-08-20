package io.github.orionlibs.orion_database_mysql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

@Transactional("PlatformTransactionManager")
public class DatabaseUpdatesDAO
{
    public void executeMultipleCommands(String SQLCode, TransactionTemplate transactionalJDBC, JdbcTemplate JDBC)
    {
        try
        {
            transactionalJDBC.execute(new TransactionCallbackWithoutResult()
            {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status)
                {
                    JDBC.execute(SQLCode);
                }
            });
        }
        catch(DataAccessException e)
        {
            logErrorToConsole(e.getMessage());
            throw e;
        }
    }


    public void runDDL(String SQLCode, TransactionTemplate transactionalJDBC, JdbcTemplate JDBC)
    {
        executeMultipleCommands(SQLCode, transactionalJDBC, JDBC);
    }


    public int runSQLUpdate(String SQLCode, Object[] parameters, JdbcTemplate JDBC)
    {
        try
        {
            if(parameters != null)
            {
                return JDBC.update(SQLCode, parameters);
            }
            else
            {
                return JDBC.update(SQLCode);
            }
        }
        catch(DuplicateKeyException e)
        {
            logErrorToConsole(e.getMessage());
            throw e;
        }
        catch(DataAccessException e)
        {
            logErrorToConsole(e.getMessage());
            throw e;
        }
    }


    public int[] runSQLBatchUpdate(String SQLCode, List<Object[]> parameters, JdbcTemplate JDBC, boolean allowSequentialExecution)
    {
        int[] numberOfRowsAffected = {};
        try
        {
            if(parameters != null)
            {
                List<Object> result = parameters.stream()
                                .flatMap(array -> Arrays.stream(array))
                                .collect(Collectors.toList());
                numberOfRowsAffected = new int[parameters.size()];
                List<Object[]> parametersMerged = new ArrayList<>();
                parametersMerged.add(result.toArray());
                numberOfRowsAffected = JDBC.batchUpdate(SQLCode, parametersMerged);
            }
            else
            {
                numberOfRowsAffected = JDBC.batchUpdate(SQLCode);
            }
        }
        catch(DataAccessException e1)
        {
            logErrorToConsole(e1.getMessage());
            if(allowSequentialExecution)
            {
                try
                {
                    if(parameters != null)
                    {
                        numberOfRowsAffected = new int[parameters.size()];
                        for(int i = 0; i < parameters.size(); i++)
                        {
                            numberOfRowsAffected[i] = runSQLUpdate(SQLCode, parameters.get(i), JDBC);
                        }
                    }
                    else
                    {
                        numberOfRowsAffected = JDBC.batchUpdate(SQLCode);
                    }
                }
                catch(DataAccessException e2)
                {
                    logErrorToConsole(e2.getMessage());
                    throw e2;
                }
            }
            else
            {
                throw e1;
            }
        }
        return numberOfRowsAffected;
    }
}