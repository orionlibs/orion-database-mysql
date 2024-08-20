package io.github.orionlibs.orion_database_mysql.sql.mysql;

import io.github.orionlibs.orion_database_mysql.sql.SQLQueryBuilderService;

public class MySQLQueryBuilderService extends SQLQueryBuilderService
{
    public MySQLQueryBuilderService()
    {
    }


    public static MySQLQueryBuilderService of()
    {
        return new MySQLQueryBuilderService();
    }


    public static String buildSearchQuery(String query)
    {
        String[] queryTokens = query.split("\\s+");
        String newQuery = "";
        if(queryTokens.length == 1)
        {
            newQuery += "+" + queryTokens[0] + "*";
        }
        else
        {
            newQuery += "";
            for(String queryToken : queryTokens)
            {
                newQuery += "+" + queryToken + "* ";
            }
            newQuery = newQuery.trim();
        }
        return newQuery;
    }
}