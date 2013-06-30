package com.example.rest.jaxrswebservice.commons;

import com.example.rest.jaxrsutils.Strings;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.sun.jersey.api.client.ClientResponse.Status;

/**
 * 
 * @author dopoljak@gmail.com
 * 
 *         Exception wrapper
 */
/**
 * 
 * @author dopoljak@gmail.com
 * 
 *         Exception wrapper
 */
public class WSExceptionWrapper
{
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(WSExceptionWrapper.class);

    @Provider
    public static class RestJaxRSException implements ExceptionMapper<JaxRSException>
    {
	@Override
	public Response toResponse(JaxRSException e)
	{
	    log.error("JaxRSException: code = {}, status = {}, message = {}", new Object[] { e.getStatus().getStatusCode(), e.getStatus(), e.getExMessage() });
	    
	    // ignore Unauthorized exception
	    if (e.getStatus().getStatusCode() != 401)
	    {
		log.error("", e);
	    }

	    return Response.status(e.getStatus()).entity(e.getExMessage()).build();
	}
    }

    @Provider
    public static class RestIllegalArgumentException implements ExceptionMapper<java.lang.IllegalArgumentException>
    {
	@Override
	public Response toResponse(java.lang.IllegalArgumentException e)
	{
	    log.error("RestIllegalArgumentException: ", e);
            String message = Strings.format("{} : {}", "java.lang.IllegalArgumentException", e.getMessage());
	    return Response.status(Status.BAD_REQUEST).entity(message).build();
	}
    }

    @Provider
    public static class RestJsonMappingException implements ExceptionMapper<com.fasterxml.jackson.databind.JsonMappingException>
    {
	@Override
	public Response toResponse(com.fasterxml.jackson.databind.JsonMappingException ex)
	{
	    log.error("RestJsonMappingException: ", ex);
	    Status status = Status.INTERNAL_SERVER_ERROR;
	    return Response.status(status).entity(ex.getMessage()).build();
	}
    }

    @Provider
    public static class RestHibernateException implements ExceptionMapper<org.hibernate.HibernateException>
    {
	@Override
	public Response toResponse(org.hibernate.HibernateException e)
	{
	    log.error("RestHibernateException: ", e);

	    Status status = Status.INTERNAL_SERVER_ERROR;
            String message = e.getMessage();
            
            if(e instanceof org.hibernate.exception.ConstraintViolationException)
            {
                status = Status.CONFLICT;
                message = Strings.format("{} : {}", "org.hibernate.exception.ConstraintViolationException", e.getMessage());
            }
            else if(e instanceof org.hibernate.QueryException)
            {
                message = Strings.format("{} : {}", "org.hibernate.QueryException", e.getMessage());
            }

	    return Response.status(status).entity(message).build();
	}
    }
}