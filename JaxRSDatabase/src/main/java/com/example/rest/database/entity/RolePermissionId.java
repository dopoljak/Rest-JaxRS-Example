package com.example.rest.database.entity;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

/**
 *
 * @author DoDo <dopoljak@gmail.com>
 */
@Embeddable
public class RolePermissionId implements Serializable
{
    private Role role;
    private Permission permission;

    @ManyToOne
    public Role getRole() {
        return role;
    }
 
    public void setRole(Role x) {
        this.role = x;
    }

    @ManyToOne
    public Permission getPermission() {
        return permission;
    }
 
    public void setPermission(Permission x) {
        this.permission = x;
    }
	
 
    @Override
	public boolean equals(Object o)
	{
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
 
        RolePermissionId that = (RolePermissionId) o;
 
        if (permission != null ? !permission.equals(that.permission) : that.permission != null) return false;
        if (role != null ? !role.equals(that.role) : that.role != null) return false;
 
        return true;
    }
 
    @Override
    public int hashCode() 
    {
        int result;
        result = (permission != null ? permission.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }
}
