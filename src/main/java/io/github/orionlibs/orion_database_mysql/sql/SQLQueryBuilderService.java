package io.github.orionlibs.orion_database_mysql.sql;

import io.github.orionlibs.orion_pagination.Pagination;
import io.github.orionlibs.orion_reflection.method.access.ReflectionMethodAccessService;
import io.github.orionlibs.orion_string.StringsService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SQLQueryBuilderService
{
    private SQLQuery query;
    public List<String> columnsToCreateParametersFor;
    private int numberOfColumnsToInsertIntoTable;
    private Object[] parameters;


    public SQLQueryBuilderService()
    {
        this.query = SQLQuery.of();
        this.columnsToCreateParametersFor = new ArrayList<String>();
    }


    public static SQLQueryBuilderService of()
    {
        return new SQLQueryBuilderService();
    }


    public SQLQuery reset()
    {
        this.columnsToCreateParametersFor = new ArrayList<String>();
        this.parameters = null;
        return this.query.reset();
    }


    public SQLQuery insertInto()
    {
        return query.append("INSERT INTO ");
    }


    public SQLQuery insertIgnoreInto()
    {
        return query.append("INSERT IGNORE INTO ");
    }


    public SQLQuery update()
    {
        return query.append("UPDATE ");
    }


    public SQLQuery where()
    {
        return query.append(" WHERE ");
    }


    public SQLQuery set()
    {
        return query.append(" SET ");
    }


    public SQLQuery select()
    {
        return query.append("SELECT ");
    }


    public SQLQuery everything()
    {
        return query.append("*");
    }


    public SQLQuery from()
    {
        return query.append(" FROM ");
    }


    public SQLQuery values()
    {
        return query.append(" VALUES ");
    }


    public SQLQuery semicolon()
    {
        return query.append(";");
    }


    public SQLQuery space()
    {
        return query.append(" ");
    }


    public SQLQuery leftParenthesis()
    {
        return query.append("(");
    }


    public SQLQuery rightParenthesis()
    {
        return query.append(")");
    }


    public SQLQuery asName(String name)
    {
        return query.append(" AS " + name);
    }


    public SQLQuery commaSpace()
    {
        return query.append(", ");
    }


    public SQLQuery join()
    {
        return query.append(" JOIN ");
    }


    public SQLQuery on()
    {
        return query.append(" ON ");
    }


    public SQLQuery and()
    {
        return query.append(" AND ");
    }


    public SQLQuery matchAgainst(List<String> columnNames, String queryParameterNameForQuestionMark)
    {
        query.append(" MATCH(");
        for(int i = 0; i < columnNames.size(); i++)
        {
            query.append(columnNames.get(i));
            if(i < columnNames.size() - 1)
            {
                query.append(", ");
            }
        }
        columnsToCreateParametersFor.add(queryParameterNameForQuestionMark);
        query.append(") AGAINST(? IN BOOLEAN MODE)");
        //query.append(") AGAINST(? IN NATURAL LANGUAGE MODE)");
        //query.append(") AGAINST(? IN NATURAL LANGUAGE MODE WITH QUERY EXPANSION)");
        return query;
    }


    public SQLQuery or()
    {
        return query.append(" OR ");
    }


    public SQLQuery fromTable(String tableName)
    {
        from();
        return query.append(tableName);
    }


    public SQLQuery groupBy()
    {
        query.append(" GROUP BY ");
        return query;
    }


    public SQLQuery groupByColumn(String columnName)
    {
        groupBy();
        query.append(columnName);
        return query;
    }


    public SQLQuery groupByColumns(List<String> columnNames)
    {
        groupBy();
        query.append(StringsService.convertToCommaAndSpaceSeparatedString(columnNames));
        return query;
    }


    public SQLQuery orderByColumn(String columnName)
    {
        query.append(" ORDER BY ");
        query.append(columnName);
        return query;
    }


    public SQLQuery ascendingOrderByColumn(String columnName)
    {
        query.append(" ORDER BY ");
        query.append(columnName);
        ascending();
        return query;
    }


    public SQLQuery ascendingOrderByColumns(List<String> columnNames)
    {
        query.append(" ORDER BY ");
        query.append(StringsService.convertToCommaAndSpaceSeparatedString(columnNames));
        ascending();
        return query;
    }


    public SQLQuery descendingOrderByColumn(String columnName)
    {
        query.append(" ORDER BY ");
        query.append(columnName);
        descending();
        return query;
    }


    public SQLQuery descendingOrderByColumns(List<String> columnNames)
    {
        query.append(" ORDER BY ");
        if(columnNames != null && !columnNames.isEmpty())
        {
            for(int i = 0; i < columnNames.size(); i++)
            {
                query.append(columnNames.get(i));
                descending();
                if(i < columnNames.size() - 1)
                {
                    query.append(", ");
                }
            }
        }
        return query;
    }


    public SQLQuery ascending()
    {
        query.append(" ASC ");
        return query;
    }


    public SQLQuery descending()
    {
        query.append(" DESC ");
        return query;
    }


    public SQLQuery limit(int limit, int offset)
    {
        if(limit > 0)
        {
            query.append(" LIMIT ");
            query.append(Integer.toString(limit));
            if(offset >= 0)
            {
                query.append(" OFFSET ");
                query.append(Integer.toString(offset));
            }
        }
        return query;
    }


    public SQLQuery limit(Pagination pagination)
    {
        if(pagination != null)
        {
            return limit(pagination.getPaginationLimit(), pagination.getOffset());
        }
        else
        {
            return query;
        }
    }


    public SQLQuery existsWithConditionConjunction(String tableName, List<String> columns)
    {
        select();
        query.append("EXISTS(");
        select();
        query.append("1");
        from();
        query.append(tableName);
        whereColumnsEqualsQuestionMarkConjunction(columns);
        return query.append(")");
    }


    public SQLQuery existsWithConditionDisjunction(String tableName, List<String> columns)
    {
        select();
        query.append("EXISTS(");
        select();
        query.append("1");
        from();
        query.append(tableName);
        whereColumnsEqualsQuestionMarkDisjunction(columns);
        return query.append(")");
    }


    public SQLQuery selectEverythingFromTable(String tableName)
    {
        select();
        everything();
        from();
        return query.append(tableName);
    }


    public SQLQuery selectDistinctEverythingFromTable(String tableName)
    {
        select();
        distinct();
        everything();
        from();
        return query.append(tableName);
    }


    public SQLQuery distinct()
    {
        return query.append(" DISTINCT ");
    }


    public SQLQuery count()
    {
        return selectColumn("COUNT(1)");
    }


    public SQLQuery selectCount()
    {
        return count();
    }


    public SQLQuery selectCountOfQueryResult(String SQLCode)
    {
        query = query.reset();
        count();
        from();
        leftParenthesis();
        query.append(SQLCode);
        rightParenthesis();
        asName("NumberOfRows");
        return query;
    }


    public SQLQuery selectColumn(String columnName)
    {
        select();
        query.append(columnName);
        return query;
    }


    public SQLQuery selectSumOfColumn(String columnName)
    {
        select();
        query.append("SUM(");
        query.append(columnName);
        query.append(")");
        return query;
    }


    public SQLQuery selectAverageOfColumn(String columnName)
    {
        select();
        query.append("AVG(");
        query.append(columnName);
        query.append(")");
        return query;
    }


    public SQLQuery selectExpression(String expression)
    {
        select();
        query.append(expression);
        return query;
    }


    public SQLQuery selectColumns(List<String> columnNames)
    {
        select();
        if(columnNames != null && !columnNames.isEmpty())
        {
            for(int i = 0; i < columnNames.size(); i++)
            {
                query.append(columnNames.get(i));
                if(i < columnNames.size() - 1)
                {
                    query.append(", ");
                }
            }
        }
        else
        {
            everything();
        }
        return query;
    }


    public SQLQuery selectTableDotEverything(String tableName)
    {
        select();
        query.append(tableName);
        return query.append(".*");
    }


    public SQLQuery selectTableDotEverythingFromTable(String tableName)
    {
        select();
        query.append(tableName);
        query.append(".*");
        from();
        return query.append(tableName);
    }


    public SQLQuery joinWithTable(String tableName)
    {
        join();
        return query.append(tableName);
    }


    public SQLQuery append(String string)
    {
        return query.append(string);
    }


    public SQLQuery ifThenElse(String condition, String thenUse, String elseUse)
    {
        query.append(" IF(");
        query.append(condition);
        query.append(", ");
        query.append(thenUse);
        query.append(", ");
        query.append(elseUse);
        return query.append(")");
    }


    public SQLQuery onColumns(Map<String, String> columnPairs)
    {
        query.append(" ON ");
        if(columnPairs.size() > 1)
        {
            leftParenthesis();
        }
        int i = 0;
        for(Map.Entry<String, String> columnPair : columnPairs.entrySet())
        {
            query.append(columnPair.getKey());
            query.append("=");
            query.append(columnPair.getValue());
            if(columnPairs.size() > 1 && i < columnPairs.size() - 1)
            {
                and();
            }
            ++i;
        }
        if(columnPairs.size() > 1)
        {
            rightParenthesis();
        }
        return query;
    }


    public SQLQuery insertIntoTable(String tableName)
    {
        insertInto();
        return table(tableName);
    }


    public SQLQuery insertIntoTableIgnoring(String tableName)
    {
        insertIgnoreInto();
        return table(tableName);
    }


    public SQLQuery updateTable(String tableName)
    {
        update();
        return table(tableName);
    }


    public SQLQuery truncateTable(String tableName)
    {
        query.append("TRUNCATE TABLE ");
        return query.append(tableName);
        //return deleteFrom(tableName);
    }


    public SQLQuery deleteFrom(String tableName)
    {
        query.append("DELETE FROM ");
        return query.append(tableName);
    }


    public SQLQuery parenthesisedCommaSeparatedColumns(List<String> columns)
    {
        columnsToCreateParametersFor.addAll(columns);
        if(columns != null && !columns.isEmpty())
        {
            numberOfColumnsToInsertIntoTable = columns.size();
            leftParenthesis();
            for(int i = 0; i < columns.size(); i++)
            {
                if(i != 0)
                {
                    query.append(", ");
                }
                query.append(columns.get(i));
            }
            rightParenthesis();
        }
        return query;
    }


    public SQLQuery columnsEqualsQuestionMark(List<String> columns)
    {
        columnsToCreateParametersFor.addAll(columns);
        if(columns != null && !columns.isEmpty())
        {
            for(int i = 0; i < columns.size(); i++)
            {
                query.append(columns.get(i));
                query.append("=?");
                if(i < columns.size() - 1)
                {
                    query.append(", ");
                }
            }
        }
        return query;
    }


    public SQLQuery whereColumnEqualsQuestionMark(String column)
    {
        where();
        return columnEqualsQuestionMark(column);
    }


    public SQLQuery whereColumnContains(String column)
    {
        where();
        columnsToCreateParametersFor.add(column);
        query.append(column);
        return query.append(" LIKE ?");
    }


    public SQLQuery whereColumnIsIn(String column, List<?> valuesOfColumn)
    {
        where();
        return columnIsIn(column, valuesOfColumn);
    }


    public SQLQuery whereColumnIsInQuestionMark(String column, List<?> valuesOfColumn)
    {
        columnsToCreateParametersFor.add(column);
        return whereColumnIsIn(column, valuesOfColumn);
    }


    public SQLQuery columnIsIn(String column, List<?> valuesOfColumn)
    {
        query.append(column);
        query.append(" IN (");
        for(int i = 0; i < valuesOfColumn.size(); i++)
        {
            if(valuesOfColumn.get(i) instanceof String)
            {
                query.append("'");
                query.append((String)valuesOfColumn.get(i));
                query.append("'");
            }
            else
            {
                query.append(valuesOfColumn.get(i).toString());
            }
            if(i < valuesOfColumn.size() - 1)
            {
                query.append(", ");
            }
        }
        return query.append(")");
    }


    public SQLQuery columnIsInExpression(String column, String expression)
    {
        query.append(column);
        query.append(" IN (");
        query.append(expression);
        return query.append(")");
    }


    public SQLQuery whereColumnNotContains(String column)
    {
        where();
        columnsToCreateParametersFor.add(column);
        query.append(column);
        return query.append(" NOT LIKE ?");
    }


    public SQLQuery whereColumnNotEqualsQuestionMark(String column)
    {
        where();
        return columnNotEqualsQuestionMark(column);
    }


    public SQLQuery columnEqualsQuestionMark(String column)
    {
        columnsToCreateParametersFor.add(column);
        query.append(column);
        return query.append("=?");
    }


    public SQLQuery columnNotEqualsQuestionMark(String column)
    {
        columnsToCreateParametersFor.add(column);
        query.append(column);
        return query.append("<>?");
    }


    public SQLQuery whereColumnWithExpression(String column, String expression)
    {
        where();
        return columnWithExpression(column, expression);
    }


    public SQLQuery columnWithExpression(String column, String expression)
    {
        query.append(column);
        return query.append(expression);
    }


    public SQLQuery expression(String expression)
    {
        return query.append(expression);
    }


    public SQLQuery columnIsNotEmpty(String column)
    {
        query.append(column);
        return query.append("<>''");
    }


    public SQLQuery columnIsNotNullAndNotEmpty(String column)
    {
        query.append(column);
        return query.append(">''");
    }


    public SQLQuery columnsWithExpressions(List<String> columnNames, List<String> expressions)
    {
        if(columnNames != null
                        && !columnNames.isEmpty()
                        && expressions != null
                        && !expressions.isEmpty()
                        && columnNames.size() == expressions.size())
        {
            for(int i = 0; i < columnNames.size(); i++)
            {
                query.append(columnNames.get(i));
                query.append("=");
                query.append(expressions.get(i));
                if(i < columnNames.size() - 1)
                {
                    query.append(", ");
                }
            }
        }
        return query;
    }


    public SQLQuery columnIsBetween(String column, Object lowerBound, Object upperBound)
    {
        query.append(column);
        space();
        query.append(">=");
        space();
        if(lowerBound instanceof String)
        {
            query.append("'");
        }
        query.append(lowerBound.toString());
        if(lowerBound instanceof String)
        {
            query.append("'");
        }
        and();
        query.append(column);
        space();
        query.append("<=");
        space();
        if(upperBound instanceof String)
        {
            query.append("'");
        }
        query.append(upperBound.toString());
        if(upperBound instanceof String)
        {
            query.append("'");
        }
        return query;
    }


    public SQLQuery whereColumnsEqualsQuestionMarkConjunction(List<String> columns)
    {
        where();
        return columnsEqualsQuestionMarkConjunction(columns);
    }


    public SQLQuery whereColumnsEqualsQuestionMarkDisjunction(List<String> columns)
    {
        where();
        return columnsEqualsQuestionMarkDisjunction(columns);
    }


    public SQLQuery columnsEqualsQuestionMarkConjunction(List<String> columns)
    {
        columnsToCreateParametersFor.addAll(columns);
        if(columns != null && !columns.isEmpty())
        {
            for(int i = 0; i < columns.size(); i++)
            {
                query.append(columns.get(i));
                query.append("=?");
                if(i < columns.size() - 1)
                {
                    query.append(" AND ");
                }
            }
        }
        return query;
    }


    public SQLQuery columnsEqualsQuestionMarkDisjunction(List<String> columns)
    {
        columnsToCreateParametersFor.addAll(columns);
        if(columns != null && !columns.isEmpty())
        {
            for(int i = 0; i < columns.size(); i++)
            {
                query.append(columns.get(i));
                query.append("=?");
                if(i < columns.size() - 1)
                {
                    query.append(" OR ");
                }
            }
        }
        return query;
    }


    public SQLQuery buildParametersArray(Object model)
    {
        if(columnsToCreateParametersFor != null && !columnsToCreateParametersFor.isEmpty())
        {
            List<Object> parametersTemp = new ArrayList<>(columnsToCreateParametersFor.size());
            for(int i = 0; i < columnsToCreateParametersFor.size(); i++)
            {
                String columnName = columnsToCreateParametersFor.get(i);
                String getterName = "get";
                if(columnName.contains("."))
                {
                    getterName += StringsService.convertFirstCharacterToUppercase(columnName.substring(columnName.lastIndexOf(".") + 1));
                }
                else
                {
                    getterName += StringsService.convertFirstCharacterToUppercase(columnName);
                }
                parametersTemp.add(ReflectionMethodAccessService.callMethod(getterName, model));
                /*Object value = ReflectionMethodAccessService.callMethod(getterName, model);
                
                if(value != null)
                {
                    parametersTemp.add(ReflectionMethodAccessService.callMethod(getterName,
                                    model));
                }
                else
                {
                    parametersTemp.add(" IS NULL");
                }*/
            }
            setParameters(parametersTemp.toArray());
        }
        return query;
    }


    public SQLQuery valuesOfParenthesisedCommaSeparatedQuestionMarks()
    {
        values();
        return parenthesisedCommaSeparatedQuestionMarks();
    }


    public SQLQuery parenthesisedCommaSeparatedQuestionMarks()
    {
        if(numberOfColumnsToInsertIntoTable > 0)
        {
            leftParenthesis();
            for(int i = 0; i < numberOfColumnsToInsertIntoTable; i++)
            {
                if(i == 0)
                {
                    query.append("?");
                }
                else
                {
                    query.append(", ?");
                }
            }
            rightParenthesis();
        }
        return query;
    }


    public SQLQuery table(String tableName)
    {
        return query.append(tableName);
    }


    public SQLQuery getQuery()
    {
        return this.query;
    }


    public void setQuery(SQLQuery query)
    {
        this.query = query;
    }


    public Object[] getParameters()
    {
        return this.parameters;
    }


    public void setParameters(Object[] parameters)
    {
        this.parameters = parameters;
    }
}