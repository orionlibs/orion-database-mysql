package io.github.orionlibs.orion_database_mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseService
{
    public static Connection getDatabaseConnection(String databaseURL, String databaseUsername, String databasePassword) throws SQLException
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(databaseURL, databaseUsername, databasePassword);
        }
        catch(ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    /*
     * private void getDatabaseEnginesFromRemoteDatabase() {
     * List<DatabaseEngineModel> databaseEngines = new ArrayList<>(); try {
     * PreparedStatement preparedStatement = connection.
     * prepareStatement("SELECT * FROM some_database_engine_name.database_engines;"
     * ); ResultSet results = preparedStatement.executeQuery();
     * while(results.next()) { DatabaseEngineModel databaseEngineModel =
     * DatabaseEngineModel.builder() .databaseURL(results.getString("databaseURL"))
     * .build(); databaseEngines.add(databaseEngineModel); } closeConnection(); }
     * catch(Exception e) { e.printStackTrace(); } }
     */


    public static void closeConnection(Connection connection)
    {
        try
        {
            if(connection != null)
            {
                connection.close();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}