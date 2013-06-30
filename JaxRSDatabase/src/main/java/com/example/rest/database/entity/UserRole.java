package com.example.rest.database.entity;

import java.io.Serializable;
import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author DoDo <dopoljak@gmail.com>
 */
@Entity
@Table(name = "UserRole")
@AssociationOverrides
({
	@AssociationOverride(name = "pk.user", 		joinColumns = @JoinColumn(name = "idUser")),
	@AssociationOverride(name = "pk.role", 		joinColumns = @JoinColumn(name = "idRole"))
})
public class UserRole implements Serializable
{
    private UserRoleId pk = new UserRoleId();

    public UserRole()
    {
    }

     @Override
    public String toString()
    {
	StringBuilder sb = new StringBuilder();
	sb.append("role = [").append(getRole()).append("]");
	sb.append(", user = [").append(getUser()).append("]");
	return sb.toString();
    }
     
    @EmbeddedId
    private UserRoleId getPk()         { return pk;      }
    private void setPk(UserRoleId pk)  { this.pk = pk;   }
 
    @Transient
    public User getUser() 		{ return getPk().getUser();     } 
    public void setUser(User x) 	{ getPk().setUser(x); 		}
 
    @Transient
    public Role getRole() 		{ return getPk().getRole();     }
    public void setRole(Role x) 	{ getPk().setRole(x); 	  	}	
}
