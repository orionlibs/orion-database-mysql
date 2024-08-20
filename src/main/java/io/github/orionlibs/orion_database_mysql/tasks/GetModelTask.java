package io.github.orionlibs.orion_database_mysql.tasks;

import io.github.orionlibs.orion_assert.Assert;
import io.github.orionlibs.orion_database.OrionModel;
import io.github.orionlibs.orion_database_mysql.MySQL;
import io.github.orionlibs.orion_database_mysql.sql.mysql.MySQLQueryBuilderService;
import java.util.List;

public class GetModelTask
{
    public static List<Object> run(OrionModel model, OrionModel emptyModel, String databaseTable, String databaseName, List<String> columnsForCondition)
    {
        Assert.notNull(model, "The given model is null.");
        Assert.notNull(emptyModel, "The given emptyModel is null.");
        Assert.notEmpty(databaseTable, "The given databaseTable is null/empty.");
        Assert.notEmpty(columnsForCondition, "The given columnsForCondition is null/empty.");
        MySQLQueryBuilderService mySQLQuery = new MySQLQueryBuilderService();
        mySQLQuery.selectEverythingFromTable(databaseName + databaseTable);
        mySQLQuery.whereColumnsEqualsQuestionMarkConjunction(columnsForCondition);
        mySQLQuery.buildParametersArray(model);
        String SQL = mySQLQuery.semicolon().toString();
        return MySQL.runSQL(SQL, emptyModel, mySQLQuery.getParameters());
    }


    public static List<Object> run(OrionModel model, OrionModel emptyModel, String databaseTable, String databaseName, List<String> columnNames, List<String> columnsForCondition)
    {
        Assert.notNull(model, "The given model is null.");
        Assert.notNull(emptyModel, "The given emptyModel is null.");
        Assert.notEmpty(databaseTable, "The given databaseTable is null/empty.");
        MySQLQueryBuilderService mySQLQuery = new MySQLQueryBuilderService();
        mySQLQuery.selectColumns(columnNames);
        mySQLQuery.fromTable(databaseName + databaseTable);
        if(columnsForCondition != null && !columnsForCondition.isEmpty())
        {
            mySQLQuery.whereColumnsEqualsQuestionMarkConjunction(columnsForCondition);
            mySQLQuery.buildParametersArray(model);
        }
        String SQL = mySQLQuery.semicolon().toString();
        return MySQL.runSQL(SQL, emptyModel, mySQLQuery.getParameters());
    }
}