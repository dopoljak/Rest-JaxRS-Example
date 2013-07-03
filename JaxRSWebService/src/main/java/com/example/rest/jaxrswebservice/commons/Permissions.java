package com.example.rest.jaxrswebservice.commons;

/**
 * Permissions for Role Based Authorization
 * 
 * @author DoDo <dopoljak@gmail.com>
 */
public enum Permissions
{
    CREATE_USER(1),
    SELECT_USER(2),
    UPDATE_USER(3),
    DELETE_USER(4);

    private long id;

    private Permissions(long id)
    {
	this.id = id;
    }

    public long getId()
    {
	return id;
    }

    public String getStringId()
    {
	return String.valueOf(id);
    }
}