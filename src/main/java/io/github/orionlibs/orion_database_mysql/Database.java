package io.github.orionlibs.orion_database_mysql;

import io.github.orionlibs.orion_database.OrionModel;
import io.github.orionlibs.orion_database.OrionSQLDatabaseAccessObject;
import io.github.orionlibs.orion_database_mysql.tasks.DeleteModelTask;
import io.github.orionlibs.orion_database_mysql.tasks.DoesRowExistWithConditionConjunctionTask;
import io.github.orionlibs.orion_database_mysql.tasks.DoesRowExistWithConditionDisjunctionTask;
import io.github.orionlibs.orion_database_mysql.tasks.GetAllRowsTask;
import io.github.orionlibs.orion_database_mysql.tasks.GetAllRowsWithAscendingOrderTask;
import io.github.orionlibs.orion_database_mysql.tasks.GetModelTask;
import io.github.orionlibs.orion_database_mysql.tasks.GetModelsWithOrderingTask;
import io.github.orionlibs.orion_database_mysql.tasks.GetNumberOfRecordsTask;
import io.github.orionlibs.orion_database_mysql.tasks.GetSumOfColumnTask;
import io.github.orionlibs.orion_database_mysql.tasks.SaveModelTask;
import io.github.orionlibs.orion_database_mysql.tasks.TruncateTableTask;
import io.github.orionlibs.orion_database_mysql.tasks.UpdateAllRowsTask;
import io.github.orionlibs.orion_database_mysql.tasks.UpdateModelTask;
import io.github.orionlibs.orion_database_mysql.tasks.UpdateModelsTask;
import io.github.orionlibs.orion_pagination.Pagination;
import java.util.List;

public class Database
{
    public static OrionSQLDatabaseAccessObject connection;
    public static String databaseName;
    public static String usersDatabaseName;
    public static String logsDatabaseName;
    public static String paymentsDatabaseName;
    public static String marketingDatabaseName;
    public static String messagesDatabaseName;
    public static String configurationDatabaseName;
    public static String geodataDatabaseName;


    public static long getSumOfColumn(String columnName, String databaseTable, String databaseName)
    {
        return GetSumOfColumnTask.run(columnName, databaseTable, databaseName);
    }


    public static long getSumOfColumn(OrionModel model, String columnName, String databaseTable, String databaseName, List<String> columnsForCondition)
    {
        return GetSumOfColumnTask.run(model, columnName, databaseTable, databaseName, columnsForCondition);
    }


    public static List<Object> getAllRows(OrionModel emptyModel, String databaseTable, String databaseName)
    {
        return GetAllRowsTask.run(emptyModel, databaseTable, databaseName);
    }


    public static List<Object> getAllRowsWithAscendingOrder(OrionModel emptyModel, String databaseTable, String databaseName, String ascendingOrderForColumn)
    {
        return GetAllRowsWithAscendingOrderTask.run(emptyModel, databaseTable, databaseName, ascendingOrderForColumn);
    }


    public static List<Object> getAllRows(OrionModel emptyModel, String databaseTable, String databaseName, List<String> columnNames)
    {
        return GetAllRowsTask.run(emptyModel, databaseTable, databaseName, columnNames);
    }


    public static boolean doesRowExistWithConditionConjunction(OrionModel model, String databaseTable, String databaseName, List<String> columnsForCondition)
    {
        return DoesRowExistWithConditionConjunctionTask.run(model, databaseTable, databaseName, columnsForCondition);
    }


    public static boolean doesRowExistWithConditionDisjunction(OrionModel model, String databaseTable, String databaseName, List<String> columnsForCondition)
    {
        return DoesRowExistWithConditionDisjunctionTask.run(model, databaseTable, databaseName, columnsForCondition);
    }


    public static long getNumberOfRecords(String databaseTable, String databaseName)
    {
        return GetNumberOfRecordsTask.run(databaseTable, databaseName);
    }


    public static long getNumberOfRecords(String SQL, Object[] parameters)
    {
        return GetNumberOfRecordsTask.run(SQL, parameters);
    }


    public static long getNumberOfRecords(OrionModel model, String databaseTable, String databaseName, List<String> columnsForCondition)
    {
        return GetNumberOfRecordsTask.run(model, databaseTable, databaseName, columnsForCondition);
    }


    public static List<Object> getModels(OrionModel model, OrionModel emptyModel, String databaseTable, String databaseName, List<String> columnsForCondition)
    {
        return GetModelTask.run(model, emptyModel, databaseTable, databaseName, columnsForCondition);
    }


    public static List<Object> getModels(OrionModel model, OrionModel emptyModel, String databaseTable, String databaseName, List<String> columnNames, List<String> columnsForCondition)
    {
        return GetModelTask.run(model, emptyModel, databaseTable, databaseName, columnNames, columnsForCondition);
    }


    public static List<Object> getModels(OrionModel model, String databaseTable, String databaseName, List<String> columnsForCondition)
    {
        return getModels(model, model, databaseTable, databaseName, columnsForCondition);
    }


    public static List<Object> getModelsWithAscendingOrder(OrionModel model, String databaseTable, String databaseName, List<String> columnsForCondition, String ascendingOrderForColumn)
    {
        return GetModelsWithOrderingTask.run(model, model, databaseTable, databaseName, columnsForCondition, ascendingOrderForColumn, true, null);
    }


    public static List<Object> getModelsWithAscendingOrder(OrionModel model, String databaseTable, String databaseName, List<String> columnNames, List<String> columnsForCondition, String ascendingOrderForColumn)
    {
        return GetModelsWithOrderingTask.run(model, model, databaseTable, databaseName, columnNames, columnsForCondition, ascendingOrderForColumn, true);
    }


    public static List<Object> getModelsWithDescendingOrder(OrionModel model, String databaseTable, String databaseName, List<String> columnsForCondition, String descendingOrderForColumn)
    {
        return GetModelsWithOrderingTask.run(model, model, databaseTable, databaseName, columnsForCondition, descendingOrderForColumn, false, null);
    }


    public static List<Object> getModelsWithDescendingOrder(OrionModel model, String databaseTable, String databaseName, List<String> columnsForCondition, String descendingOrderForColumn, Pagination pagination)
    {
        return GetModelsWithOrderingTask.run(model, model, databaseTable, databaseName, columnsForCondition, descendingOrderForColumn, false, pagination);
    }


    public static List<Object> getModelsWithAscendingOrder(OrionModel model, String databaseTable, String databaseName, List<String> columnsForCondition, String asscendingOrderForColumn, Pagination pagination)
    {
        return GetModelsWithOrderingTask.run(model, model, databaseTable, databaseName, columnsForCondition, asscendingOrderForColumn, true, pagination);
    }


    public static List<Object> getModelsWithDescendingOrder(OrionModel model, String databaseTable, String databaseName, List<String> columnNames, List<String> columnsForCondition, String descendingOrderForColumn)
    {
        return GetModelsWithOrderingTask.run(model, model, databaseTable, databaseName, columnNames, columnsForCondition, descendingOrderForColumn, false);
    }


    public static Object getOneModel(OrionModel model, OrionModel emptyModel, String databaseTable, String databaseName, List<String> columnsForCondition)
    {
        List<Object> temp = getModels(model, emptyModel, databaseTable, databaseName, columnsForCondition);
        if(temp != null && !temp.isEmpty())
        {
            return temp.get(0);
        }
        return null;
    }


    public static Object getOneModel(OrionModel model, OrionModel emptyModel, String databaseTable, String databaseName, List<String> columnNames, List<String> columnsForCondition)
    {
        List<Object> temp = getModels(model, emptyModel, databaseTable, databaseName, columnNames, columnsForCondition);
        if(temp != null && !temp.isEmpty())
        {
            return temp.get(0);
        }
        return null;
    }


    public static Object getOneModel(OrionModel model, String databaseTable, String databaseName, List<String> columnsForCondition)
    {
        return getOneModel(model, model, databaseTable, databaseName, columnsForCondition);
    }


    public static int saveModel(OrionModel model, String databaseTable, String databaseName, List<String> columnsToSave)
    {
        return SaveModelTask.run(model, databaseTable, databaseName, columnsToSave);
    }


    public static int saveModel(OrionModel model, String databaseTable, String databaseName)
    {
        return SaveModelTask.run(model, databaseTable, databaseName);
    }


    public static int updateModel(OrionModel model, String databaseTable, String databaseName, List<String> columnsToUpdate, List<String> columnsForCondition)
    {
        return UpdateModelTask.run(model, databaseTable, databaseName, columnsToUpdate, columnsForCondition);
    }


    public static int updateModel(OrionModel model, String databaseTable, String databaseName, List<String> columnsForCondition)
    {
        return UpdateModelTask.run(model, databaseTable, databaseName, columnsForCondition);
    }


    public static int[] updateModels(List<OrionModel> models, String databaseTable, String databaseName, List<String> columnsToUpdate, List<String> columnsForCondition, boolean allowSequentialExecution)
    {
        return UpdateModelsTask.run(models, databaseTable, databaseName, columnsToUpdate, columnsForCondition, allowSequentialExecution);
    }


    public static int[] updateModels(List<OrionModel> models, String databaseTable, String databaseName, List<String> columnsForCondition, boolean allowSequentialExecution)
    {
        return UpdateModelsTask.run(models, databaseTable, databaseName, columnsForCondition, allowSequentialExecution);
    }


    public static int updateAllRows(OrionModel model, String databaseTable, String databaseName, List<String> columnsToUpdate)
    {
        return UpdateAllRowsTask.run(model, databaseTable, databaseName, columnsToUpdate);
    }


    public static int deleteModel(OrionModel model, String databaseTable, String databaseName, List<String> columnsForCondition)
    {
        return DeleteModelTask.run(model, databaseTable, databaseName, columnsForCondition);
    }


    public static int truncateTable(String databaseTable, String databaseName)
    {
        return TruncateTableTask.run(databaseTable, databaseName);
    }


    public static int runSQL(String SQLCode)
    {
        return connection.runSQL(SQLCode);
    }


    public static int runSQL(String SQLCode, Object[] parameters)
    {
        return connection.runSQL(SQLCode, parameters);
    }


    public static void executeMultipleCommands(String SQLCode)
    {
        connection.executeMultipleCommands(SQLCode);
    }


    public static void runDDL(String SQLCode)
    {
        connection.runDDL(SQLCode);
    }


    public static int[] runSQLBatch(String SQLCode, List<Object[]> parameters, boolean allowSequentialExecution)
    {
        return connection.runSQLBatch(SQLCode, parameters, allowSequentialExecution);
    }


    public static int[] runSQLBatch(String SQLCode, boolean allowSequentialExecution)
    {
        return connection.runSQLBatch(SQLCode, allowSequentialExecution);
    }


    public static List<Object> runSQL(String SQLCode, Object modelToUse, Object[] parameters)
    {
        return connection.runSQL(SQLCode, modelToUse, parameters);
    }


    public static List<Object> runSQL(String SQLCode, Object modelToUse)
    {
        return connection.runSQL(SQLCode, modelToUse);
    }
}