package com.example.rest.database.dao;

import com.example.rest.database.base.GenericDAO;
import com.example.rest.database.entity.UserRole;
import java.io.Serializable;

/**
 *
 * @author DoDo <dopoljak@gmail.com>
 */
public class UserRoleDAO extends GenericDAO<UserRole, Serializable>
{
    public UserRoleDAO()
    {
	super(UserRole.class);
    }
}
