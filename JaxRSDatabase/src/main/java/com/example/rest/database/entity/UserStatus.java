package com.example.rest.database.entity;

import com.example.rest.database.base.AuditField;
import com.example.rest.database.base.IEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * @author DoDo <dopoljak@gmail.com>
 */
@JsonIgnoreProperties(value = { "admins" })
@Entity
@Table(name = "USERSTATUS")
public class UserStatus implements java.io.Serializable, IEntity 
{
    public static final transient Long ACTIVE = 1L;
    public static final transient Long BLOCKED = 2L;
    public static final transient Long DELETED = 3L;
    
    private Long id;
    private String name;

    public UserStatus() {
    }

    public UserStatus(Long id)
    {
        this.id = id;
    }

    @Id
    @Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "NAME", length = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
	StringBuilder sb = new StringBuilder();
	sb.append("id = ").append(id);
	sb.append(", name = ").append(name);
	return sb.toString();
    }

    public void validateMandatoryFields() throws IllegalArgumentException
    {
        
    }

    @Transient
    public AuditField[] getAuditFields()
    {
        return null;
    }
}
