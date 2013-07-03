package com.example.rest.jaxrswebservice.service;

import static com.example.rest.jaxrsutils.Strings.format;
import com.example.rest.database.dao.UserDAO;
import com.example.rest.database.dao.UserRoleDAO;
import com.example.rest.database.entity.Role;
import com.example.rest.database.entity.User;
import com.example.rest.database.entity.UserRole;
import com.example.rest.database.entity.UserStatus;
import com.example.rest.jaxrsutils.Hashs;
import com.example.rest.jaxrsutils.Strings;
import com.example.rest.jaxrswebservice.commons.JaxRSException;
import com.example.rest.jaxrswebservice.commons.Permissions;
import com.example.rest.jaxrswebservice.commons.RequiresPermissions;
import com.example.rest.jaxrswebservice.commons.RequiresTransaction;
import com.example.rest.jaxrswebservice.commons.Wrapper;
import com.example.rest.jaxrswebservice.interceptor.Intercepted;
import com.sun.jersey.api.client.ClientResponse.Status;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * REST Service for User resource
 * 
 * @author DoDo <dopoljak@gmail.com>
 */

@Path("/users")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsersWS
{
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UsersWS.class);
    private static final UserDAO userDAO = new UserDAO();
    private static final UserRoleDAO userRoleDAO = new UserRoleDAO();
    
    @GET
    @RequiresTransaction
    @RequiresPermissions(Permissions.SELECT_USER)
    @Intercepted
    public Wrapper<List<User>> getAll(
            @QueryParam(value = "limit") @DefaultValue("50") Integer limit,
            @QueryParam(value = "offset") @DefaultValue("0") Integer offset,
            @QueryParam(value = "order") @DefaultValue("asc") String order,
            @QueryParam(value = "sort") @DefaultValue("id") String sort)
    {
	log.info("entry: getAll() : limit = {}, offset = {}, sort = {}, order = {}", new Object[] { limit, offset, sort, order });

	// DB: count all & load filtered
	final Long total = userDAO.count();
	final List<User> list = userDAO.find(limit, offset, order, sort);

	log.info("total size of all Users = {}, number of loaded Users = {}", total, list.size());

	// SEND: response wrapper
	final Wrapper<List<User>> returnMe = new Wrapper<List<User>>();
	returnMe.setTotal(total);
	returnMe.setData(list);

	log.info("exit: getAll()");
	return returnMe;
    }
    
    
    @POST
    @RequiresTransaction
    @RequiresPermissions(Permissions.CREATE_USER)
    @Intercepted
    public Wrapper<User> create(User user) throws NoSuchAlgorithmException
    {
	log.info("entry: create(), User = {}", user.toString());

	// DB: load Admin with given name
	User existing = userDAO.findByUsername(user.getUsername());
	if (existing != null)
	{
	    throw new JaxRSException(Status.CONFLICT, format("User with given username = '{}' already exists in database!", user.getUsername()));
	}

	// add additional fields
	user.setCreated(new Date());
	user.setUserstatus(new UserStatus(UserStatus.ACTIVE));
	
	// hash password
	if(!Strings.isEmpty(user.getPassword())) {
	    String sha256Password = Strings.toHEX(Hashs.getSHA256(user.getPassword().getBytes())); 
	    user.setPassword(sha256Password);
	}

	// DB: save admin
	userDAO.save(user);
	log.info("created User with id = {}", user.getId());

        // DB: add roles to admin
        if(user.getIdRoles() != null)
        {
            for (Long idRole : user.getIdRoles())
            {
                UserRole userRole = new UserRole();
                userRole.setRole(new Role(idRole));
                userRole.setUser(user);
                userRoleDAO.save(userRole);
            }
        }

	log.info("exit: create()");
	return new Wrapper<User>(null);
    }
}
