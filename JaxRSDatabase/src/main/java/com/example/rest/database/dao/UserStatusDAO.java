package com.example.rest.database.dao;

import com.example.rest.database.base.GenericDAO;
import com.example.rest.database.entity.UserRole;
import com.example.rest.database.entity.UserStatus;
import java.io.Serializable;

/**
 *
 * @author DoDo <dopoljak@gmail.com>
 */
public class UserStatusDAO extends GenericDAO<UserStatus, Serializable>
{
    public UserStatusDAO()
    {
	super(UserStatus.class);
    }
}