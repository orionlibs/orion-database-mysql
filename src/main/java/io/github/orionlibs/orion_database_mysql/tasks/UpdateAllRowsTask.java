package io.github.orionlibs.orion_database_mysql.tasks;

import com.orion.core.security.DataSecurityService;
import com.orion.core.security.NoEncodingAndEncryptionAlgorithmsForUsernameProvidedException;
import io.github.orionlibs.orion_assert.Assert;
import io.github.orionlibs.orion_database.OrionModel;
import io.github.orionlibs.orion_database_mysql.Database;
import io.github.orionlibs.orion_database_mysql.sql.mysql.MySQLQueryBuilderService;
import java.util.List;

public class UpdateAllRowsTask
{
    public static int run(OrionModel model, String databaseTable, String databaseName, List<String> columnsToUpdate)
    {
        Assert.notNull(model, "The given model is null.");
        Assert.notEmpty(databaseTable, "The given databaseTable is null/empty.");
        Assert.notEmpty(columnsToUpdate, "The given columnsToUpdate is null/empty.");
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
        mySQLQuery.updateTable(databaseName + databaseTable);
        mySQLQuery.set();
        mySQLQuery.columnsEqualsQuestionMark(columnsToUpdate);
        mySQLQuery.buildParametersArray(modelCopy);
        String SQL = mySQLQuery.semicolon().toString();
        return Database.runSQL(SQL, mySQLQuery.getParameters());
    }
}