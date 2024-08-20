package io.github.orionlibs.orion_database_mysql.tasks;

import com.orion.core.security.DataSecurityService;
import com.orion.core.security.NoEncodingAndEncryptionAlgorithmsForUsernameProvidedException;
import io.github.orionlibs.orion_assert.Assert;
import io.github.orionlibs.orion_database.OrionModel;
import io.github.orionlibs.orion_database_mysql.Database;
import io.github.orionlibs.orion_database_mysql.sql.mysql.MySQLQueryBuilderService;
import java.util.List;

public class GetSumOfColumnTask
{
    public static long run(String columnName, String databaseTable, String databaseName)
    {
        Assert.notEmpty(columnName, "The given columnName is null/empty.");
        Assert.notEmpty(databaseTable, "The given databaseTable is null/empty.");
        MySQLQueryBuilderService mySQLQuery = new MySQLQueryBuilderService();
        mySQLQuery.selectColumn("SUM(" + columnName + ")");
        mySQLQuery.fromTable(databaseName + databaseTable);
        String SQL = mySQLQuery.semicolon().toString();
        List<Object> temp = Database.runSQL(SQL, Long.valueOf(0));
        if(temp != null && !temp.isEmpty() && temp.get(0) != null)
        {
            return (long)temp.get(0);
        }
        return 0L;
    }


    public static long run(OrionModel model, String columnName, String databaseTable, String databaseName, List<String> columnsForCondition)
    {
        Assert.notEmpty(columnName, "The given columnName is null/empty.");
        Assert.notEmpty(databaseTable, "The given databaseTable is null/empty.");
        OrionModel modelCopy = model.getCopy();
        try
        {
            DataSecurityService.encryptObject(modelCopy);
        }
        catch(NoEncodingAndEncryptionAlgorithmsForUsernameProvidedException e)
        {
            //
        }
        MySQLQueryBuilderService mySQLQuery = new MySQLQueryBuilderService();
        mySQLQuery.selectColumn("SUM(" + columnName + ")");
        mySQLQuery.fromTable(databaseName + databaseTable);
        mySQLQuery.whereColumnsEqualsQuestionMarkConjunction(columnsForCondition);
        String SQL = mySQLQuery.semicolon().toString();
        mySQLQuery.buildParametersArray(modelCopy);
        List<Object> temp = Database.runSQL(SQL, Long.valueOf(0), mySQLQuery.getParameters());
        if(temp != null && !temp.isEmpty() && temp.get(0) != null)
        {
            return (long)temp.get(0);
        }
        return 0L;
    }
}