package io.github.orionlibs.orion_database_mysql.tasks;

import com.orion.core.security.DataSecurityService;
import com.orion.core.security.NoEncodingAndEncryptionAlgorithmsForUsernameProvidedException;
import io.github.orionlibs.orion_assert.Assert;
import io.github.orionlibs.orion_database.OrionModel;
import io.github.orionlibs.orion_database_mysql.Database;
import io.github.orionlibs.orion_database_mysql.sql.mysql.MySQLQueryBuilderService;
import io.github.orionlibs.orion_pagination.Pagination;
import java.util.List;

public class GetModelsWithOrderingTask
{
    public static List<Object> run(OrionModel model, OrionModel emptyModel, String databaseTable, String databaseName, List<String> columnsForCondition, String columnToOrderBy, boolean ascendingOrder, Pagination pagination)
    {
        Assert.notNull(model, "The given model is null.");
        Assert.notNull(emptyModel, "The given emptyModel is null.");
        Assert.notEmpty(databaseTable, "The given databaseTable is null/empty.");
        Assert.notEmpty(columnsForCondition, "The given columnsForCondition is null/empty.");
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
        mySQLQuery.selectEverythingFromTable(databaseName + databaseTable);
        mySQLQuery.whereColumnsEqualsQuestionMarkConjunction(columnsForCondition);
        if(ascendingOrder)
        {
            mySQLQuery.ascendingOrderByColumn(columnToOrderBy);
        }
        else
        {
            mySQLQuery.descendingOrderByColumn(columnToOrderBy);
        }
        if(pagination != null)
        {
            mySQLQuery.limit(pagination);
        }
        mySQLQuery.buildParametersArray(modelCopy);
        String SQL = mySQLQuery.semicolon().toString();
        return Database.runSQL(SQL, emptyModel, mySQLQuery.getParameters());
    }


    public static List<Object> run(OrionModel model, OrionModel emptyModel, String databaseTable, String databaseName, List<String> columnNames, List<String> columnsForCondition, String columnToOrderBy, boolean ascendingOrder)
    {
        Assert.notNull(model, "The given model is null.");
        Assert.notNull(emptyModel, "The given emptyModel is null.");
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
        mySQLQuery.selectColumns(columnNames);
        mySQLQuery.fromTable(databaseName + databaseTable);
        if(columnsForCondition != null && !columnsForCondition.isEmpty())
        {
            mySQLQuery.whereColumnsEqualsQuestionMarkConjunction(columnsForCondition);
            mySQLQuery.buildParametersArray(modelCopy);
        }
        if(ascendingOrder)
        {
            mySQLQuery.ascendingOrderByColumn(columnToOrderBy);
        }
        else
        {
            mySQLQuery.descendingOrderByColumn(columnToOrderBy);
        }
        String SQL = mySQLQuery.semicolon().toString();
        return Database.runSQL(SQL, emptyModel, mySQLQuery.getParameters());
    }
}