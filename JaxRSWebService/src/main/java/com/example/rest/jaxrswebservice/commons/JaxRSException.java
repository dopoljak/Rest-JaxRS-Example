package com.example.rest.jaxrswebservice.commons;

import javax.ws.rs.WebApplicationException;

import com.sun.jersey.api.client.ClientResponse.Status;

/**
 * @author dpoljak
 * 
 *         Main exception class with HTTP Status code & message
 * 
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
