package com.example.rest.jaxrswebservice.filters;

import com.example.rest.database.HibernateUtil;
import com.example.rest.jaxrsutils.Strings;
import com.example.rest.jaxrswebservice.commons.Permissions;
import com.example.rest.jaxrswebservice.commons.RequiresTransaction;
import com.example.rest.jaxrswebservice.commons.RequiresPermissions;
import com.example.rest.jaxrswebservice.commons.UserSession;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;
import com.sun.jersey.spi.container.ResourceFilterFactory;
import javax.servlet.http.HttpSession;

/**
 * 	   This is Jersey (JAX-RS) based resource filter which intercepts all requests 
 * 	   directed to '/API/*' (all services written using JAX-RS) URL.
 * 
 *         ### PERMISSION FILTER ###
 * 
 * 	   Filter first checks for @RequiresPermissions annotation in method & class 
 * 	   and if founds one, it extracts USER ROLES from request (which are injected in
 *         Authentication filter) and check if user have required ROLE which
 *         includes needed PERMISSION for authorization purpose
 * 
 *         ### TRANSACTION FILTER ###
 * 
 *         Transaction filter, which manages transaction for each class/method
 *         that has @RequiresTransaction annotation. This filter with throw
 *         error if transaction can't be opened or committed. Exception will be
 *         cough in WSExceptionWrapper class.
 * 
 *         ### AUDITLOG FILTER ### 
 *         
 *         AuditLog, if request was successful new AuditLog record is created 
 *         ( after original transaction log was committed )
 *         
 *         AuditLog is created only for methods which have @RequiresPermissions annotations
 *         and method which returns status code from 200 to 299 (HTTP OK status codes) 
 *         
 *         AuditLog record is created in separated database transaction
 * 
 *  @author DoDo <dopoljak@gmail.com>
 */
public class JAXFilter implements ResourceFilterFactory
{
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(JAXFilter.class);

    private @Context HttpServletRequest httpServletRequest;

    private class RequestResponseFilter implements ContainerRequestFilter, ContainerResponseFilter, ResourceFilter
    {
	private Permissions permission;
	private RequiresTransaction transaction;
	private UserSession userSession;

	public RequestResponseFilter(RequiresPermissions permission, RequiresTransaction transaction)
	{
	    if (permission != null)
	    {
		this.permission = permission.value();
	    }
	    if (transaction != null)
	    {
		this.transaction = transaction;
	    }
	}

	@Override
	public ContainerRequestFilter getRequestFilter()
	{
	    return this;
	}

	@Override
	public ContainerResponseFilter getResponseFilter()
	{
	    return this;
	}

	@Override
	public ContainerRequest filter(ContainerRequest request)
	{
            final HttpSession session = httpServletRequest.getSession(true);

            // set current locale
            //Lang.setCurrentLanguage(String.valueOf(session.getAttribute(UserSession.LANGUAGE)));

	    if (permission != null)
	    {
		userSession = (UserSession) session.getAttribute(UserSession.USER_SESSION);

		/**
		 * if session is null, re-authenticate
		 **/
		if (userSession == null)
		{
		    final String error = "User is not logged-in, please authenticate first!";
		    log.info("{}", error);
		    throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED).entity(error).build());
		}

		/**
		 * check if UserSession is Active or user contains required
		 * permission
		 **/
		if (userSession.getPermissions() != null && userSession.getPermissions().contains(permission.getStringId()))
		{
		    log.trace("User contains required permission!");
		}
		else
		{
		    final String error = Strings.format("User = '{}' don't have required permission = '{}'", userSession, permission);
		    log.info("{}", error);
		    throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity(error).build());
		}
	    }

	    if (transaction != null)
	    {
		log.debug("Begining transaction ...");                
		HibernateUtil.beginTransaction();
	    }

	    return request;
	}

	@Override
	public ContainerResponse filter(ContainerRequest request, ContainerResponse response)
	{
	    if (transaction != null)
	    {
		log.debug("Commiting or rollbacking transaction ...");
		HibernateUtil.commitTransactionOrRollback();
	    }

            /*
	    if (permission != null && userSession != null && (response.getStatus() >= 200 && response.getStatus() <= 299))
	    {
		StringBuilder queryDetails = new StringBuilder();
		queryDetails.append(request.getMethod()).append(" ");
		queryDetails.append(request.getPath());

		MultivaluedMap<String, String> queryParams = request.getQueryParameters();
		for (String key : queryParams.keySet())
		{
		    if (!key.equalsIgnoreCase("_dc") && !key.equalsIgnoreCase("page"))
		    {
			String value = queryParams.get(key).get(0);
			queryDetails.append(" ").append(key).append("=").append(value);
		    }
		}

		AuditLog audit = new AuditLog();
		audit.setAdmin(userSession.getAdmin());
		audit.setCreateddate(new Date());
		audit.setDescription(queryDetails.toString());
		audit.setFinancialinstitution(userSession.getFinInst());
		audit.setType(new AdminAuthority(permission.getId()));
		//response.getStatus()

		HibernateUtil.beginTransaction();

		auditLogDAO.save(audit);
		log.info("Created new AuditLog = {}", audit);

		HibernateUtil.commitTransactionOrRollback();
	    }*/

	    return response;
	}
    }

    @Override
    public List<ResourceFilter> create(AbstractMethod am)
    {
	log.trace("##### create method = {}, is annotation PERMISSION present = {}, is TRANSACTION present = {}", new Object[] { am.getMethod(), am.isAnnotationPresent(RequiresPermissions.class), am.isAnnotationPresent(RequiresTransaction.class) });

	/** Permission: METHOD permission take precedence over CLASS permission **/
	RequiresPermissions permission = am.getAnnotation(RequiresPermissions.class);
	if (permission == null)
	{
	    /** Permission: CLASS permission **/
	    permission = am.getResource().getAnnotation(RequiresPermissions.class);
	}

	/**
	 * Transaction: METHOD annotation takes precedence over CLASS
	 * transaction
	 */
	RequiresTransaction transaction = am.getAnnotation(RequiresTransaction.class);
	if (transaction == null)
	{
	    /** Transaction: CLASS transaction **/
	    transaction = am.getResource().getAnnotation(RequiresTransaction.class);
	}

	return Collections.<ResourceFilter> singletonList(new RequestResponseFilter(permission, transaction));
    }
}
