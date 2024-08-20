package io.github.orionlibs.orion_database_mysql;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(
                {java.lang.annotation.ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface IgnoreForORM
{
}