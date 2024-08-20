package io.github.orionlibs.orion_database_mysql.tasks;

import io.github.orionlibs.orion_assert.Assert;
import io.github.orionlibs.orion_database.OrionModel;
import io.github.orionlibs.orion_database_mysql.Database;
import io.github.orionlibs.orion_database_mysql.sql.mysql.MySQLQueryBuilderService;
import java.util.List;

public class GetAllRowsWithAscendingOrderTask
{
    public static List<Object> run(OrionModel emptyModel, String databaseTable, String databaseName, String ascendingOrderForColumn)
    {
        Assert.notNull(emptyModel, "The given emptyModel is null.");
        Assert.notEmpty(databaseTable, "The given databaseTable is null/empty.");
        MySQLQueryBuilderService mySQLQuery = new MySQLQueryBuilderService();
        mySQLQuery.selectEverythingFromTable(databaseName + databaseTable);
        mySQLQuery.ascendingOrderByColumn(ascendingOrderForColumn);
        String SQL = mySQLQuery.semicolon().toString();
        return Database.runSQL(SQL, emptyModel);
    }
}