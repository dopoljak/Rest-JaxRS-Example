package com.example.rest.jaxrswebservice.commons;

import com.example.rest.database.HibernateUtil;
import com.example.rest.jaxrsutils.Strings;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.sun.jersey.api.client.ClientResponse.Status;
import javax.ws.rs.WebApplicationException;
import org.hibernate.HibernateException;

/**
 * Exception wrapper
 * 
 * @author DoDo <dopoljak@gmail.com>
 */
public class WSExceptionWrapper
{
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(WSExceptionWrapper.class);

    @Provider
    public static class RestException implements ExceptionMapper<Exception>
    {
	@Override
	public Response toResponse(Exception ex)
	{
            log.info("If active rollback transaction ...");
	    HibernateUtil.rollbackTransactionSilently();

            if(ex instanceof JaxRSException)
            {
                JaxRSException e = (JaxRSException) ex;
                log.error("JaxRSException: code = {}, status = {}, message = {}", new Object[] { e.getStatus().getStatusCode(), e.getStatus(), e.getExMessage() });

                // ignore Unauthorized exception
                if (e.getStatus().getStatusCode() != 401)
                {
                    log.error("", e);
                }

                return Response.status(e.getStatus()).entity(e.getExMessage()).build();
            }
            else if(ex instanceof IllegalArgumentException)
            {
                log.error("IllegalArgumentException: ", ex);
                String message = Strings.format("{} : {}", "java.lang.IllegalArgumentException", ex.getMessage());
                return Response.status(Status.BAD_REQUEST).entity(message).build();
            }
            else if(ex instanceof com.fasterxml.jackson.databind.JsonMappingException)
            {
                log.error("JsonMappingException: ", ex);
                Status status = Status.INTERNAL_SERVER_ERROR;
                return Response.status(status).entity(ex.getMessage()).build();
            }
            else if (ex instanceof org.hibernate.HibernateException)
            {
                HibernateException e = (HibernateException) ex;
                log.error("HibernateException: ", e);

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
            else if(ex instanceof WebApplicationException)
            {
                Response response = ((WebApplicationException)ex).getResponse();
                log.error("WebApplicationException: status = {}, entity = {}", response.getStatus(), response.getEntity());
                return response;
            }
            else
            {
                log.error("", ex);
                Status status = Status.INTERNAL_SERVER_ERROR;
                return Response.status(status).entity(ex.getMessage()).build();
            }
	}
    }
}