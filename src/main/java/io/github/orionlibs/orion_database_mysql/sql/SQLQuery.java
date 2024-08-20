package io.github.orionlibs.orion_database_mysql.sql;

public class SQLQuery
{
    private StringBuilder sb;


    public SQLQuery()
    {
        this.sb = new StringBuilder();
    }


    public static SQLQuery of()
    {
        return new SQLQuery();
    }


    public SQLQuery append(String text)
    {
        this.sb.append(text);
        return this;
    }


    public SQLQuery reset()
    {
        this.sb = new StringBuilder();
        return this;
    }


    public String toString()
    {
        return this.sb.toString();
    }
}