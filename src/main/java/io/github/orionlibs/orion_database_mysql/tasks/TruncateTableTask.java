package io.github.orionlibs.orion_database_mysql.tasks;

import io.github.orionlibs.orion_assert.Assert;
import io.github.orionlibs.orion_database_mysql.MySQL;
import io.github.orionlibs.orion_database_mysql.sql.mysql.MySQLQueryBuilderService;

public class TruncateTableTask
{
    public static int run(String databaseTable, String databaseName)
    {
        Assert.notEmpty(databaseTable, "The given databaseTable is null/empty.");
        MySQLQueryBuilderService mySQLQuery = new MySQLQueryBuilderService();
        mySQLQuery.truncateTable(databaseName + databaseTable);
        String SQL = mySQLQuery.semicolon().toString();
        return MySQL.runSQL(SQL);
    }
}