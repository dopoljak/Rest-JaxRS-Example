package com.example.rest.database.base;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used for Auditing fields
 * 
 * @author DoDo <dopoljak@gmail.com>
 */
@Target( value = { ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD } )
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface IAuditedField
{
    int fieldIndex();
}
