package com.example.rest.database.entity;

import com.example.rest.database.base.AuditField;
import com.example.rest.database.base.IEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author DoDo <dopoljak@gmail.com>
 */
@JsonIgnoreProperties(value = { "userRoles", "auditFields", "hibernateLazyInitializer", "handler" })
@Entity
@Table(name = "USER")
public class User implements java.io.Serializable, IEntity
{
    private Long id;
    private String username;
    private String password;    
    private String firstname;
    private String lastname;
    private String email;    
    private Date created;
    private UserStatus userstatus;
    private Set<UserRole> userRoles = new HashSet<UserRole>(0);
    
    // JSON DTO properties
    private Set<Long> idRoles;

    public User() 
    {
    }

    @Id
    @GeneratedValue
    @Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    @Column(name = "USERNAME", length = 160)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    
    @Column(name = "PASSWORD", length = 160)
     public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "FIRSTNAME", length = 160)
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @Column(name = "LASTNAME", length = 160)
    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Column(name = "EMAIL", length = 200)
    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }
    
    
    

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED", nullable = false, length = 7)
    public Date getCreated()
    {
	return this.created;
    }

    public void setCreated(Date created)
    {
	this.created = created;
    }
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDUSERSTATUS")
    public UserStatus getUserstatus() {
        return userstatus;
    }

    public void setUserstatus(UserStatus userstatus) {
        this.userstatus = userstatus;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "pk.user")
    public Set<UserRole> getUserRoles()
    {
        return userRoles;
    }

    public void setUserRoles(Set<UserRole> userRoles)
    {
        this.userRoles = userRoles;
    }


    @Override
    public String toString()
    {
	final StringBuilder sb = new StringBuilder();
	sb.append("id = ").append(id);
	sb.append(", userstatus [").append(userstatus).append("]");
	sb.append(", firstname = ").append(firstname);
	sb.append(", lastname = ").append(lastname);
	sb.append(", created = ").append(created);
	return sb.toString();
    }


    @Override
    public void validateMandatoryFields() throws IllegalArgumentException
    {
        if(username == null || username.isEmpty())
        {
            throw new IllegalArgumentException("Username field is NULL or Empty");
        }
    }

    @Override
    @Transient
    public AuditField[] getAuditFields()
    {
        AuditField auditFields[] = 
        {
            new AuditField(1, id),
            new AuditField(2, username),
            new AuditField(3, password)
        };

        return auditFields;
    }
    
    
    
    /**
     * JSON DTO properties
     */
    @Transient
    public Set<Long> getIdRoles()
    {
	return idRoles;
    }

    public void setIdRoles(Set<Long> idRoles)
    {
	this.idRoles = idRoles;
    }
}
