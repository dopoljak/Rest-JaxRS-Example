package com.example.rest.database.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author dopoljak@gmail.com
 */
@JsonIgnoreProperties(value = { "" })
@Entity
@Table(name = "ROLE")
public class Role implements Serializable
{
    private Long id;
    private String name;
    private Date created;    
    private Set<RolePermission>	rolePermissions	= new HashSet<RolePermission>(0);

    public Role()
    {
    }

    public Role(Long id)
    {
        this.id = id;
    }

    @Override
    public String toString()
    {
	final StringBuilder sb = new StringBuilder();
	sb.append("id = ").append(id);
	sb.append(", name = ").append(name);
	sb.append(", created = ").append(created);
	return sb.toString();
    }
     

    @Id
    @GeneratedValue
    @Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    @Column(name = "NAME", length = 200)
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED", nullable = false, length = 7)
    public Date getCreated()
    {
        return created;
    }

    public void setCreated(Date created)
    {
        this.created = created;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "pk.role")
    public Set<RolePermission> getRolePermissions()
    {
        return rolePermissions;
    }

    public void setRolePermissions(Set<RolePermission> rolePermissions)
    {
        this.rolePermissions = rolePermissions;
    }
     
}
