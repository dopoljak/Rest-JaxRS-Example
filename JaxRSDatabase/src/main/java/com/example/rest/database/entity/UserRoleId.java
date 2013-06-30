package com.example.rest.database.entity;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

/**
 *
 * @author DoDo <dopoljak@gmail.com>
 */
@Embeddable
public class UserRoleId implements Serializable
{
    private User user;
    private Role role;

     
    @ManyToOne
    public User getUser() {
        return user;
    }
 
	public void setUser(User x) {
		this.user = x;
	}
 

	@ManyToOne
	public Role getRole() {
		return role;
	}
 
	public void setRole(Role x) {
		this.role = x;
	}
	
 
	public boolean equals(Object o)
	{
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
 
        UserRoleId that = (UserRoleId) o;
 
        if (user != null ? !user.equals(that.user) : that.user != null) return false;
        if (role != null ? !role.equals(that.role) : that.role != null) return false;
 
        return true;
    }
 
    public int hashCode() 
    {
        int result;
        result = (user != null ? user.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }
}
