package com.example.rest.jaxrswebservice.service;

import com.example.rest.database.dao.UserDAO;
import com.example.rest.database.entity.Permission;
import com.example.rest.database.entity.Role;
import com.example.rest.database.entity.RolePermission;
import com.example.rest.database.entity.User;
import com.example.rest.database.entity.UserRole;
import com.example.rest.database.entity.UserStatus;
import com.example.rest.jaxrsutils.Strings;
import com.example.rest.jaxrswebservice.commons.JaxRSException;
import com.example.rest.jaxrswebservice.commons.RequiresTransaction;
import com.example.rest.jaxrswebservice.commons.UserSession;

import static com.example.rest.jaxrsutils.Strings.format;

import com.sun.jersey.api.client.ClientResponse.Status;
import javax.enterprise.context.RequestScoped;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * REST Service for Session resource
 * 
 * @author DoDo <dopoljak@gmail.com>
 */
@RequestScoped
@Path("/sessions")
public class SessionWS
{
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SessionWS.class);
    private static final UserDAO userDAO = new UserDAO();
    private HttpServletRequest request;
    
    public SessionWS(@Context HttpServletRequest request)
    {
	this.request = request;
    }
    
    
    @GET
    @Consumes({ MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON })
    @Produces(MediaType.APPLICATION_JSON)
    @RequiresTransaction
    public UserSession getCurrentUserSession()
    {
	log.info("entry: getCurrentUserSession()");

	// get session
	UserSession userSession = (UserSession) request.getSession(true).getAttribute(UserSession.USER_SESSION);

	// check session
	if (userSession == null)
	{
	    log.error("### Session is null, enter username & password!");
	    throw new JaxRSException(Status.UNAUTHORIZED, "Session is null, enter username & password!");
	}

	log.info("UserSession is = {}", userSession);

	log.info("exit: getCurrentUserSession()");
	return userSession;
    }

    @POST
    @RequiresTransaction
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public UserSession create(@FormParam(value = "username") String username, @FormParam(value = "password") String password) throws NoSuchAlgorithmException, IOException
    {
	log.info("entry: create(), username = {}, password len = {}", username, password != null ? password.length() : "NULL" );

	// check if username & password is entered
	if (Strings.isBlank(username) || Strings.isBlank(password))
	{
	    log.error("Session is null, enter username & password!");
	    throw new JaxRSException(Status.UNAUTHORIZED, "Session is null, enter username & password!");
	}

	// DB: load User from Database
        User user = userDAO.findByUsernamePassword(username, password);
	if (user == null)
	{
	    log.error("User doesn't exist, cant find username = {} / password len = {} pair in database", username, password.length());
	    throw new JaxRSException(Status.UNAUTHORIZED, format("User doesn't exist, cant find username = {} / password len = {} pair in database", username, password.length()));
	}
	
	// check if user is ACTIVE or BLOCKED
        UserStatus status = user.getUserstatus();
	if(UserStatus.BLOCKED.equals(user.getId()) || UserStatus.DELETED.equals(user.getId()))
	{
	    log.error("User username = {} is in status = {}", username, status.getName());
	    throw new JaxRSException(Status.UNAUTHORIZED, format("User username = {}, is in status = {}", username, status.getName()) );
	}

        // load all roles & permissions
        Set<UserRole> userRoles = user.getUserRoles();
        if (userRoles == null)
	{
	    log.error("User (username = {}) doesn't not have defined roles!", username);
	    throw new JaxRSException(Status.UNAUTHORIZED, format("User (username = {}) doesn't not have defined roles!", username));
	}

	// populate list with permission from roles
	Set<String> permissionIDs = new HashSet<String>();
	for (UserRole userRole : userRoles)
	{
            Role role = userRole.getRole();
	    if (role != null)
	    {
		Set<RolePermission> rolePermissions = role.getRolePermissions();

		log.info("For Username = {}, founded Role = {}, and Permissions size = {}", new Object[] { username, role.getName(), rolePermissions.size() });

		for (RolePermission rolePermission : rolePermissions)
		{
                    Permission permission = rolePermission.getPermission();
		    if (permission != null)
		    {
			log.debug("For Role = {}, founded Permission = {}", new Object[] { role.getName(), permission.getName() } );
			permissionIDs.add(String.valueOf(permission.getId()));
		    }
		}
	    }
	}

	// check permission
	if (permissionIDs.size() <= 0)
	{
	    log.error("User with username = {}, doesn't have defined permission!", username);
	    throw new JaxRSException(Status.UNAUTHORIZED, format("User with username = {}, doesn't have defined authorities!", username));
	}

	log.info("For username = {}, loaded permissions are = {}", username, permissionIDs.toString());

	// create session data
	UserSession userSession = new UserSession();
        userSession.setPermissions(permissionIDs);
	userSession.setUser(user);

        // add to session
	request.getSession(true).setAttribute(UserSession.USER_SESSION, userSession);
	
	log.info("Created new session = {}", userSession);

	log.info("exit: create()");
	return (userSession);
    }

    @DELETE
    @RequiresTransaction
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public UserSession logoutDeleteSession()
    {
	log.info("entry: logoutDeleteSession()");

	// get session
	UserSession userSession = (UserSession) request.getSession(true).getAttribute(UserSession.USER_SESSION);

	// check session
	if (userSession == null)
	{
	    log.error("### Session is null, enter username & password!");
	    throw new JaxRSException(Status.UNAUTHORIZED, "Session is null, enter username & password!");
	}

	log.info("Deleting UserSession = {}", userSession);

	// delete session
	request.getSession(true).invalidate();

	log.info("exit: logoutDeleteSession()");
	return userSession;
    }
    
}
