package com.example.rest.jaxrswebservice.commons;

import javax.ws.rs.WebApplicationException;

import com.sun.jersey.api.client.ClientResponse.Status;

/**
 * Main exception class with HTTP Status code & message
 * 
 * @author DoDo <dopoljak@gmail.com>
 */
public class JaxRSException extends WebApplicationException
{
    private static final long serialVersionUID = -6564278935788010968L;

    private com.sun.jersey.api.client.ClientResponse.Status status;
    private String exMessage;

    public JaxRSException(Status status, String exMessage)
    {
	this.status = status;
	this.exMessage = exMessage;
    }

    public String getExMessage()
    {
	return exMessage;
    }

    public Status getStatus()
    {
	return status;
    }
}
