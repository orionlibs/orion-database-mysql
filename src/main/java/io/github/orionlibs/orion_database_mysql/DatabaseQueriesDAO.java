package io.github.orionlibs.orion_database_mysql;

import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class DatabaseQueriesDAO
{
    @SuppressWarnings("unchecked")
    public List<Object> runSQLQuery(String SQLCode, Object modelToUse, Object[] parameters, JdbcTemplate JDBC)
    {
        try
        {
            if(modelToUse.getClass() == Long.class
                            || modelToUse.getClass() == Integer.class
                            || modelToUse.getClass() == Short.class
                            || modelToUse.getClass() == Byte.class
                            || modelToUse.getClass() == Float.class
                            || modelToUse.getClass() == Double.class
                            || modelToUse.getClass() == Boolean.class
                            || modelToUse.getClass() == String.class
                            || modelToUse.getClass() == Character.class)
            {
                return DatabaseQueriesForPrimitivesDAO.queryForPrimitiveType(SQLCode, modelToUse, parameters, JDBC);
            }
            else
            {
                if(parameters != null)
                {
                    return JDBC.query(SQLCode, new OrionBeanPropertyRowMapper<Object>((Class<Object>)modelToUse.getClass()), parameters);
                }
                else
                {
                    return JDBC.query(SQLCode, new OrionBeanPropertyRowMapper<Object>((Class<Object>)modelToUse.getClass()));
                }
            }
        }
        catch(DataAccessException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw e;
        }
    }
}