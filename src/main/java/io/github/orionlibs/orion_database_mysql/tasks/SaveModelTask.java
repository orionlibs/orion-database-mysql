package io.github.orionlibs.orion_database_mysql.tasks;

import io.github.orionlibs.orion_assert.Assert;
import io.github.orionlibs.orion_database.OrionModel;
import io.github.orionlibs.orion_database_mysql.IgnoreForORM;
import io.github.orionlibs.orion_database_mysql.MySQL;
import io.github.orionlibs.orion_database_mysql.sql.mysql.MySQLQueryBuilderService;
import io.github.orionlibs.orion_reflection.variable.access.ReflectionInstanceVariablesAccessService;
import io.github.orionlibs.orion_reflection.variable.retrieval.ReflectionInstanceVariablesRetrievalService;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SaveModelTask
{
    public static int run(OrionModel model, String databaseTable, String databaseName, List<String> columnsToSave)
    {
        Assert.notNull(model, "The given model is null.");
        Assert.notEmpty(databaseTable, "The given databaseTable is null/empty.");
        Assert.notEmpty(columnsToSave, "The given columnsToSave is null/empty.");
        MySQLQueryBuilderService mySQLQuery = new MySQLQueryBuilderService();
        mySQLQuery.insertIntoTable(databaseName + databaseTable);
        mySQLQuery.parenthesisedCommaSeparatedColumns(columnsToSave);
        mySQLQuery.valuesOfParenthesisedCommaSeparatedQuestionMarks();
        mySQLQuery.buildParametersArray(model);
        String SQL = mySQLQuery.semicolon().toString();
        return MySQL.runSQL(SQL, mySQLQuery.getParameters());
    }


    public static int run(OrionModel model, String databaseTable, String databaseName)
    {
        Assert.notNull(model, "The given model is null.");
        Assert.notEmpty(databaseTable, "The given databaseTable is null/empty.");
        MySQLQueryBuilderService mySQLQuery = new MySQLQueryBuilderService();
        mySQLQuery.insertIntoTable(databaseName + databaseTable);
        List<String> columnsToSave = new ArrayList<>();
        List<Field> privateInstanceVariables = ReflectionInstanceVariablesRetrievalService.getAllPrivateInstanceVariables(model);
        for(Field field : privateInstanceVariables)
        {
            ReflectionInstanceVariablesAccessService.makeInstanceVariableAccessible(field);
            IgnoreForORM ignoreForORMAnnotation = field.getAnnotation(IgnoreForORM.class);
            if(ignoreForORMAnnotation == null)
            {
                columnsToSave.add(field.getName());
            }
        }
        mySQLQuery.parenthesisedCommaSeparatedColumns(columnsToSave);
        mySQLQuery.valuesOfParenthesisedCommaSeparatedQuestionMarks();
        mySQLQuery.buildParametersArray(model);
        String SQL = mySQLQuery.semicolon().toString();
        return MySQL.runSQL(SQL, mySQLQuery.getParameters());
    }
}