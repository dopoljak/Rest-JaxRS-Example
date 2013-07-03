package com.example.rest.database.base;

import javax.persistence.Transient;

/**
 * Base entity inteface to provide support for validation and audit log
 * 
 * @author DoDo <dopoljak@gmail.com>
 */
public interface IEntity
{
    void validateMandatoryFields() throws IllegalArgumentException;

    @Transient
    AuditField[] getAuditFields();
}
