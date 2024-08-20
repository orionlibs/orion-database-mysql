package io.github.orionlibs.orion_database_mysql.tasks;

import com.orion.core.security.DataSecurityService;
import com.orion.core.security.NoEncodingAndEncryptionAlgorithmsForUsernameProvidedException;
import io.github.orionlibs.orion_assert.Assert;
import io.github.orionlibs.orion_database.OrionModel;
import io.github.orionlibs.orion_database_mysql.Database;
import io.github.orionlibs.orion_database_mysql.sql.mysql.MySQLQueryBuilderService;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class UpdateModelTask
{
    public static int run(OrionModel model, String databaseTable, String databaseName, List<String> columnsToUpdate, List<String> columnsForCondition)
    {
        Assert.notNull(model, "The given model is null.");
        Assert.notEmpty(databaseTable, "The given databaseTable is null/empty.");
        Assert.notEmpty(columnsToUpdate, "The given columnsToUpdate is null/empty.");
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
        mySQLQuery.updateTable(databaseName + databaseTable);
        mySQLQuery.set();
        mySQLQuery.columnsEqualsQuestionMark(columnsToUpdate);
        mySQLQuery.whereColumnsEqualsQuestionMarkConjunction(columnsForCondition);
        mySQLQuery.buildParametersArray(modelCopy);
        String SQL = mySQLQuery.semicolon().toString();
        return Database.runSQL(SQL, mySQLQuery.getParameters());
    }


    public static int run(OrionModel model, String databaseTable, String databaseName, List<String> columnsForCondition)
    {
        Assert.notNull(model, "The given model is null.");
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
        mySQLQuery.updateTable(databaseName + databaseTable);
        mySQLQuery.set();
        List<String> columnsToUpdate = new ArrayList<>();
        List<Field> privateInstanceVariables = ReflectionInstanceVariablesRetrievalService.getAllPrivateInstanceVariables(modelCopy);
        for(Field field : privateInstanceVariables)
        {
            ReflectionInstanceVariablesAccessService.makeInstanceVariableAccessible(field);
            IgnoreForORM ignoreForORMAnnotation = field.getAnnotation(IgnoreForORM.class);
            if(ignoreForORMAnnotation == null)
            {
                columnsToUpdate.add(field.getName());
            }
        }
        mySQLQuery.columnsEqualsQuestionMark(columnsToUpdate);
        mySQLQuery.whereColumnsEqualsQuestionMarkConjunction(columnsForCondition);
        mySQLQuery.buildParametersArray(modelCopy);
        String SQL = mySQLQuery.semicolon().toString();
        return Database.runSQL(SQL, mySQLQuery.getParameters());
    }
}