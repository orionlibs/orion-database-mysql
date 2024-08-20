package io.github.orionlibs.orion_database_mysql;

import io.github.orionlibs.orion_calendar.SQLTimestamp;
import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

public class OrionBeanPropertyRowMapper<T> extends BeanPropertyRowMapper<T>
{
    private Class<T> mappedClass;
    private Map<String, PropertyDescriptor> mappedFields;
    private Set<String> mappedProperties;
    private boolean primitivesDefaultedForNullValue;


    public OrionBeanPropertyRowMapper()
    {
    }


    public OrionBeanPropertyRowMapper(Class<T> mappedClass)
    {
        initialize(mappedClass);
    }


    public static <T> OrionBeanPropertyRowMapper<T> of()
    {
        return new OrionBeanPropertyRowMapper<T>();
    }


    @Override
    protected void initialize(Class<T> mappedClass)
    {
        this.mappedClass = mappedClass;
        this.mappedFields = new HashMap<String, PropertyDescriptor>();
        this.mappedProperties = new HashSet<String>();
        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(mappedClass);
        Arrays.stream(pds)
                        .filter(pd -> pd.getWriteMethod() != null)
                        .forEach(pd -> setupDescriptorsOfMappedFields(pd));
    }


    private void setupDescriptorsOfMappedFields(PropertyDescriptor pd)
    {
        this.mappedFields.put(lowerCaseName(pd.getName()), pd);
        String underscoredName = underscoreName(pd.getName());
        if(!(lowerCaseName(pd.getName()).equals(underscoredName)))
        {
            this.mappedFields.put(underscoredName, pd);
        }
        this.mappedProperties.add(pd.getName());
    }


    @Override
    public void setMappedClass(Class<T> mappedClass) throws InvalidDataAccessApiUsageException
    {
        if(this.mappedClass == null)
        {
            initialize(mappedClass);
        }
        else if(this.mappedClass != mappedClass)
        {
            String errorMessage = new StringBuilder()
                            .append("The mapped class can not be reassigned to map to ")
                            .append(mappedClass)
                            .append(" since it is already providing mapping for ")
                            .append(getMappedClass())
                            .toString();
            this.logger.error(errorMessage);
            throw new InvalidDataAccessApiUsageException("There was an error.");
        }
    }


    @SuppressWarnings("unchecked")
    @Override
    public T mapRow(ResultSet rs, int rowNumber) throws BeanInstantiationException, SQLException, TypeMismatchException, DataRetrievalFailureException, InvalidDataAccessApiUsageException
    {
        Assert.state(this.mappedClass != null, "Mapped class was not specified");
        Object mappedObject = BeanUtils.instantiateClass(this.mappedClass);
        BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(mappedObject);
        initBeanWrapper(bw);
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        Set<String> populatedProperties = (isCheckFullyPopulated()) ? new HashSet<String>() : null;
        for(int index = 1; index <= columnCount; ++index)
        {
            mapColumnToField(rs, rowNumber, mappedObject, bw, rsmd, populatedProperties, index);
        }
        if((populatedProperties != null) && (!(populatedProperties.equals(this.mappedProperties))))
        {
            String errorMessage = new StringBuilder()
                            .append("Given ResultSet does not contain all fields necessary to populate object of class [")
                            .append(this.mappedClass.getName())
                            .append("]: ")
                            .append(this.mappedProperties)
                            .toString();
            this.logger.error(errorMessage);
            throw new InvalidDataAccessApiUsageException("There was an error.");
        }
        return (T)mappedObject;
    }


    private void mapColumnToField(ResultSet rs, int rowNumber, Object mappedObject, BeanWrapper bw, ResultSetMetaData rsmd, Set<String> populatedProperties, int index) throws SQLException
    {
        String column = JdbcUtils.lookupColumnName(rsmd, index);
        String field = lowerCaseName(column.replace(" ", ""));
        PropertyDescriptor pd = this.mappedFields.get(field);
        if(pd != null)
        {
            try
            {
                Object value = getColumnValue(rs, index, pd);
                try
                {
                    if(value != null && value.getClass().getName().equals("java.time.LocalDateTime"))
                    {
                        java.time.LocalDateTime valueLocalDateTime = (java.time.LocalDateTime)value;
                        bw.setPropertyValue(pd.getName(), SQLTimestamp.of(1000 * valueLocalDateTime.toEpochSecond(ZoneOffset.UTC)));
                    }
                    else
                    {
                        bw.setPropertyValue(pd.getName(), value);
                    }
                }
                catch(TypeMismatchException e)
                {
                    if(value == null && this.primitivesDefaultedForNullValue)
                    {
                        logNullValueForPrimitiveTypedColumn(rowNumber, mappedObject, column, pd, e);
                    }
                    throw e;
                }
                if(populatedProperties != null)
                {
                    populatedProperties.add(pd.getName());
                }
            }
            catch(NotWritablePropertyException e)
            {
                String errorMessage = new StringBuilder()
                                .append("Unable to map column '")
                                .append(column)
                                .append("' to property '")
                                .append(pd.getName())
                                .append("'")
                                .toString();
                this.logger.error(errorMessage);
                throw new DataRetrievalFailureException("There was an error.");
            }
        }
        else if(rowNumber == 0)
        {
            String errorMessage = new StringBuilder()
                            .append("No property found for column '")
                            .append(column)
                            .append("' mapped to field '")
                            .append(field)
                            .append("'")
                            .toString();
            this.logger.debug(errorMessage);
        }
    }


    private void logNullValueForPrimitiveTypedColumn(int rowNumber, Object mappedObject, String column, PropertyDescriptor pd, TypeMismatchException e)
    {
        String errorMessage = new StringBuilder()
                        .append("Intercepted TypeMismatchException for row ")
                        .append(rowNumber)
                        .append(" and column '")
                        .append(column)
                        .append("' with null value when setting property '")
                        .append(pd.getName())
                        .append("' of type '")
                        .append(ClassUtils.getQualifiedName(pd.getPropertyType()))
                        .append("' on object: ")
                        .append(mappedObject)
                        .toString();
        this.logger.debug(errorMessage, e);
    }
}