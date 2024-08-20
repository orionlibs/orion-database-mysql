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

public class UpdateModelsTask
{
    public static int[] run(List<OrionModel> models, String databaseTable, String databaseName, List<String> columnsToUpdate, List<String> columnsForCondition, boolean allowSequentialExecution)
    {
        Assert.notNull(models, "The given models is null.");
        Assert.notEmpty(databaseTable, "The given databaseTable is null/empty.");
        Assert.notEmpty(columnsToUpdate, "The given columnsToUpdate is null/empty.");
        Assert.notEmpty(columnsForCondition, "The given columnsForCondition is null/empty.");
        if(!models.isEmpty())
        {
            String SQL = "";
            List<Object[]> parameters = new ArrayList<>(models.size());
            MySQLQueryBuilderService mySQLQuery = new MySQLQueryBuilderService();
            for(OrionModel model : models)
            {
                mySQLQuery.reset();
                mySQLQuery.updateTable(databaseName + databaseTable);
                mySQLQuery.set();
                mySQLQuery.columnsEqualsQuestionMark(columnsToUpdate);
                mySQLQuery.whereColumnsEqualsQuestionMarkConjunction(columnsForCondition);
                mySQLQuery.buildParametersArray(model);
                SQL += mySQLQuery.semicolon().toString();
                parameters.add(mySQLQuery.getParameters());
            }
            return MySQL.runSQLBatch(SQL, parameters, allowSequentialExecution);
        }
        return null;
    }


    public static int[] run(List<OrionModel> models, String databaseTable, String databaseName, List<String> columnsForCondition, boolean allowSequentialExecution)
    {
        Assert.notNull(models, "The given models is null.");
        Assert.notEmpty(databaseTable, "The given databaseTable is null/empty.");
        Assert.notEmpty(columnsForCondition, "The given columnsForCondition is null/empty.");
        if(!models.isEmpty())
        {
            String SQL = "";
            List<Object[]> parameters = new ArrayList<>(models.size());
            MySQLQueryBuilderService mySQLQuery = new MySQLQueryBuilderService();
            for(OrionModel model : models)
            {
                mySQLQuery.reset();
                mySQLQuery.updateTable(databaseName + databaseTable);
                mySQLQuery.set();
                List<String> columnsToUpdate = new ArrayList<>();
                List<Field> privateInstanceVariables = ReflectionInstanceVariablesRetrievalService.getAllPrivateInstanceVariables(model);
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
                mySQLQuery.buildParametersArray(model);
                SQL += mySQLQuery.semicolon().toString();
                parameters.add(mySQLQuery.getParameters());
            }
            return MySQL.runSQLBatch(SQL, parameters, allowSequentialExecution);
        }
        return null;
    }
}