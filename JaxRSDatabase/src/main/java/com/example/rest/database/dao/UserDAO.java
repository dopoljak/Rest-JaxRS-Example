package com.example.rest.database.dao;

import com.example.rest.database.entity.User;
import com.example.rest.database.entity.UserStatus;
import com.example.rest.database.base.GenericDAO;
import com.example.rest.jaxrsutils.Hashs;
import com.example.rest.jaxrsutils.Strings;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 * 
 * @author DoDo <dopoljak@gmail.com>
 */
public class UserDAO  extends GenericDAO<User, Serializable>
{
    public UserDAO()
    {
	super(User.class);
    }

    /**
     * Find User by USERNAME and NOT DELETED
     */
    public User findActiveByUsername(final String username)
    {
	Criteria criteria = getSession().createCriteria(dataClass);
	criteria.add(Restrictions.eq("userstatus.id", UserStatus.ACTIVE));
	criteria.add(Restrictions.eq("username", username));
	return (User) criteria.uniqueResult();
    }

    /**
    * Find username by USERNAME + PASSWORD
    */
    public User findByUsernamePassword(String username, String password) throws NoSuchAlgorithmException
    {
        Criteria criteria = getSession().createCriteria(dataClass);
	criteria.add(Restrictions.eq("username", username));
	String sha256Password = Strings.toHEX(Hashs.getSHA256(password.getBytes()));
	criteria.add(Restrictions.eq("password", sha256Password).ignoreCase());
	return (User) criteria.uniqueResult();
    }

    /**
     * Find User by USERNAME and NOT DELETED
     */
    public User findByUsername(final String username)
    {
	Criteria criteria = getSession().createCriteria(dataClass);
	criteria.add(Restrictions.ne("userstatus.id", UserStatus.DELETED));
	criteria.add(Restrictions.eq("username", username));
	return (User) criteria.uniqueResult();
    }
}