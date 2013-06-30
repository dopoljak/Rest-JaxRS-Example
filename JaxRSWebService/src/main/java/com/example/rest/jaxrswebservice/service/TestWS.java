package com.example.rest.jaxrswebservice.service;

import com.example.rest.database.HibernateUtil;
import com.example.rest.database.dao.UserDAO;
import com.example.rest.database.entity.User;
import com.example.rest.jaxrswebservice.commons.Permissions;
import com.example.rest.jaxrswebservice.commons.RequiresPermissions;
import com.example.rest.jaxrswebservice.commons.RequiresTransaction;
import com.example.rest.jaxrswebservice.interceptor.Intercepted;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


//@Stateless
@RequestScoped
@Path("/test")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TestWS
{
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TestWS.class);

    public TestWS()
    {
        log.info("################ TEST WS ################");
    }    
    
    
    @GET
    @RequiresTransaction
    @RequiresPermissions(Permissions.SELECT_USER)
    public List<User> getAllUsers()
    {
        log.info("getAllUsers");
    
        UserDAO userDAO = new UserDAO();
        List<User> users = userDAO.findAll();

        for (User user : users) {
            System.out.println("...main .... = " + user);
            log.info("User = {}", user);
        }
    
        return users;
    }

    @GET
    @Path("/post")
    public Response postUser()
    {
        log.info("postUser ...");
        
        HibernateUtil.beginTransaction();
 
        UserDAO userDAO = new UserDAO();
        
        User newuser = new User();
        newuser.setFirstname("firstname");
        newuser.setCreated(new Date());
        userDAO.save(newuser);

        HibernateUtil.commitTransactionOrRollback();
        
        return Response.status(200).entity("getUser is called").build();
    }
    
    @GET
    @Path("/vip")
    @Intercepted
    public Response getUserVIP()
    {
        log.info("getUserVIP ...");
        return Response.status(200).entity("getUserVIP is called").build();
    }
    
}
