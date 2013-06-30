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
@Table(name = "ROLEPERMISSION")
@AssociationOverrides
({
    @AssociationOverride(name = "pk.role",          joinColumns = @JoinColumn(name = "idRole")),
    @AssociationOverride(name = "pk.permission",    joinColumns = @JoinColumn(name = "idPermission"))
})
public class RolePermission implements Serializable
{
    private RolePermissionId pk = new RolePermissionId();
	
    @EmbeddedId
    private RolePermissionId getPk() 		{ return pk; 	}
    private void setPk(RolePermissionId pk) 	{ this.pk = pk; }
 
    @Transient
    public Role getRole()                       { return getPk().getRole(); }
    public void setRole(Role x)                 { getPk().setRole(x);       }

    @Transient
    public Permission getPermission()           { return getPk().getPermission();   }
    public void setPermission(Permission x) 	{ getPk().setPermission(x);         }

    public RolePermission()
    {
    }
    

}
