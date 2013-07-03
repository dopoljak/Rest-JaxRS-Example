package com.example.rest.jaxrswebservice.commons;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation interface for Hibernate transaction purpose
 * 
 * @author DoDo <dopoljak@gmail.com>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface RequiresTransaction
{

}
