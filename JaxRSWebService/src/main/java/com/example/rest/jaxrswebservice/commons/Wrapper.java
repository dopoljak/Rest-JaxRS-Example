package com.example.rest.jaxrswebservice.commons;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Wrapper class which unifies all JSON/XML responses
 * 
 * @author DoDo <dopoljak@gmail.com>
 */
@XmlRootElement
@XmlType(propOrder = { "total", "data" })
@JsonInclude(Include.NON_NULL)
public class Wrapper<T>
{
    private Long total;
    private T data;
    // private String next;

    public Wrapper()
    {
    }

    public Wrapper(T data)
    {
	this.data = data;
    }

    public Wrapper(String status, T data, String message)
    {
	this.data = data;
    }

    public T getData()
    {
	return data;
    }

    public void setData(T data)
    {
	this.data = data;
    }

    public Long getTotal()
    {
	return total;
    }

    public void setTotal(Long totalCount)
    {
	this.total = totalCount;
    }
}