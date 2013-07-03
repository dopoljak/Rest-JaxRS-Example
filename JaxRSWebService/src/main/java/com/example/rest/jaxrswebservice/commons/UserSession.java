package com.example.rest.jaxrswebservice.commons;

import com.example.rest.database.entity.User;
import java.util.Set;

/**
 * Class that store user parameters for current session
 * 
 * @author DoDo <dopoljak@gmail.com>
 */
public class UserSession
{
    public static final String USER_SESSION = "USER_SESSION";
    public static final String LANGUAGE     = "LANGUAGE";

    private Set<String> permissions;
    private User user;

    @Override
    public String toString()
    {
	final StringBuilder sb = new StringBuilder();
	if (user != null)
	{
	    sb.append("id = ").append(user.getId());
	    sb.append(", username = ").append(user.getUsername());
	}
	return sb.toString();
    }

    public User getUser()
    {
	return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<String> getPermissions()
    {
        return permissions;
    }

    public void setPermissions(Set<String> permissions)
    {
        this.permissions = permissions;
    }
}
