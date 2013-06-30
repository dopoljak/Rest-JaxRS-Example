package com.example.rest.database.dao;

import com.example.rest.database.base.GenericDAO;
import com.example.rest.database.entity.Role;
import java.io.Serializable;

/**
 *
 * @author DoDo <dopoljak@gmail.com>
 */
public class RoleDAO extends GenericDAO<Role, Serializable>
{
    public RoleDAO()
    {
	super(Role.class);
    }
}

