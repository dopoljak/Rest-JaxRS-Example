package com.example.rest.database.dao;

import com.example.rest.database.base.GenericDAO;
import com.example.rest.database.entity.RolePermission;
import java.io.Serializable;

/**
 *
 * @author DoDo <dopoljak@gmail.com>
 */
public class RolePermissionDAO extends GenericDAO<RolePermission, Serializable>
{
    public RolePermissionDAO()
    {
	super(RolePermission.class);
    }
}
