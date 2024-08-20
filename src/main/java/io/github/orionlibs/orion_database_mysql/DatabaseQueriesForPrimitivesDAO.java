package io.github.orionlibs.orion_database_mysql;

import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class DatabaseQueriesForPrimitivesDAO
{
    @SuppressWarnings("unchecked")
    public static List<Object> queryForPrimitiveType(String SQLCode, Object modelToUse, Object[] parameters, JdbcTemplate JDBC) throws DataAccessException
    {
        try
        {
            if(parameters != null)
            {
                return (List<Object>)JDBC.queryForList(SQLCode, modelToUse.getClass(), parameters);
            }
            else
            {
                return (List<Object>)JDBC.queryForList(SQLCode, modelToUse.getClass());
            }
        }
        catch(EmptyResultDataAccessException e)
        {
            return null;
        }
    }
}