package com.example.rest.database.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author DoDo <dopoljak@gmail.com>
 */
@Entity
@Table(name = "PERMISSON")
public class Permission implements Serializable
{
    private Long id;
    private String name;

    public Permission()
    {
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
}
