package com.example.rest.database.dao;

import com.example.rest.database.base.GenericDAO;
import com.example.rest.database.entity.Permission;
import java.io.Serializable;

/**
 *
 * @author DoDo <dopoljak@gmail.com>
 */
public class PermissionDAO extends GenericDAO<Permission, Serializable>
{
     public PermissionDAO()
    {
	super(Permission.class);
    }
}
