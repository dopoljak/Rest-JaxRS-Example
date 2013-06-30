package com.example.rest.jaxrswebservice.configuration;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import com.example.rest.database.HibernateUtil;
import com.example.rest.database.dao.PermissionDAO;
import com.example.rest.database.dao.RoleDAO;
import com.example.rest.database.dao.RolePermissionDAO;
import com.example.rest.database.dao.UserDAO;
import com.example.rest.database.dao.UserRoleDAO;
import com.example.rest.database.dao.UserStatusDAO;
import com.example.rest.database.entity.Permission;
import com.example.rest.database.entity.Role;
import com.example.rest.database.entity.RolePermission;
import com.example.rest.database.entity.User;
import com.example.rest.database.entity.UserRole;
import com.example.rest.database.entity.UserStatus;
import com.example.rest.jaxrsutils.Hashs;
import com.example.rest.jaxrsutils.Strings;
import com.example.rest.jaxrswebservice.commons.Permissions;
import java.util.Date;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dpoljak
 */
public class InitializationServlet  extends HttpServlet
{
    public static final long START_TIME = System.currentTimeMillis();
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(InitializationServlet.class);

    @Override
    public void init(ServletConfig servletConfig) throws ServletException
    {
	final String oldThreadName = Thread.currentThread().getName();
	Thread.currentThread().setName("InitializationServlet");

	/**
	 * Load parameters from servlet config
	 */
	Config.initialize(servletConfig);

	
        
        /** initialize logger **/
        
        // ROOT LOGGER
        Logger rootLogger = (Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        LoggerContext loggerContext = rootLogger.getLoggerContext();
        // we are not interested in auto-configuration
        loggerContext.reset();
        
        // PATERN
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.setPattern("%d [%thread] %highlight(%-5level) %cyan(%logger{0}) - %msg %n");
        encoder.start();

        // CONSOLE APPENDER
        ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<ILoggingEvent>();
        appender.setContext(loggerContext);
        appender.setEncoder(encoder); 
        appender.start();
        rootLogger.addAppender(appender);
        
        // SET LOG LEVEL
        rootLogger.setLevel(Level.INFO);

        // HIBERNATE LOGGER
        Logger hiberLogger = (Logger)LoggerFactory.getLogger("org.hibernate");
        hiberLogger.setLevel(Level.INFO);

        // WELD LOGGER
        Logger weldLogger = (Logger)LoggerFactory.getLogger("org.jboss.weld");
        weldLogger.setLevel(Level.INFO);

        
	/** DUMP all configuration properties to LOG **/
	log.info("Loaded configuration properties are: {}", Config.getInitializedParameters());

	/** initialize database **/
	log.info("Hibernate initialization ...");
	HibernateUtil.initialize(Config.getDBProperties());

        /*
        String JaxRSWebService  = POMUtils.getVersion("com.example", "JaxRSWebService");
        String JaxRSUtils  = POMUtils.getVersion("com.example", "JaxRSUtils");
        String JaxRSDatabase     = POMUtils.getVersion("com.example", "JaxRSDatabase");

	log.info("Started JaxRSWebService = {}, JaxRSUtils = {}, JaxRSDatabase = {}", new Object[] { JaxRSWebService, JaxRSUtils, JaxRSDatabase } );
	System.out.println("Started JaxRSWebService = " + JaxRSWebService + ", JaxRSUtils = " + JaxRSUtils + ", JaxRSDatabase = " + JaxRSDatabase);
        */

        log.info("Started JaxRSWebService ");
	System.out.println("Started JaxRSWebService ");

        try 
        {            
            log.info("started inserting test data ...");
            
            UserDAO userDAO = new UserDAO();
            PermissionDAO permissionDAO = new PermissionDAO();
            RoleDAO roleDAO = new RoleDAO();
            RolePermissionDAO rolePermissionDAO = new RolePermissionDAO();
            UserRoleDAO userRoleDAO = new UserRoleDAO();

            HibernateUtil.beginTransaction();
            
            // INSERT USER
            User user = new User();
            user.setCreated(new Date());
            user.setEmail("user1@email.com");
            user.setFirstname("dodo");
            user.setLastname("dodo");
            user.setUsername("dodo");
            user.setPassword(Strings.toHEX(Hashs.getSHA256("dodo".getBytes())));
            userDAO.save(user);
            
            // INSER NEW ROLE
            Role role = new Role();
            role.setCreated(new Date());
            role.setName("role#1");
            roleDAO.save(role);
            
            // LINK USER TO ROLE
            UserRole userRole = new UserRole();
            userRole.setUser(user);
            userRole.setRole(role);
            userRoleDAO.save(userRole);
            
            // INSERT PERMISSIONS & LINK TO ROLE
            for (Permissions permission : Permissions.values()) 
            {
                Permission perm = new Permission();
                perm.setId(permission.getId());
                perm.setName(permission.toString());
                permissionDAO.save(perm);
                
                RolePermission rolePermission = new RolePermission();
                rolePermission.setPermission(perm);
                rolePermission.setRole(role);
                rolePermissionDAO.save(rolePermission);
            }

            UserStatus userStatus1 = new UserStatus(1L);
            userStatus1.setName("ACTIVE");
            UserStatus userStatus2 = new UserStatus(2L);
            userStatus2.setName("BLOCKED");
            UserStatus userStatus3 = new UserStatus(3L);
            userStatus3.setName("DELETED");
            
            UserStatusDAO userStatusDAO = new UserStatusDAO();
            userStatusDAO.save(userStatus1);            
            userStatusDAO.save(userStatus2);
            userStatusDAO.save(userStatus3);
            
            HibernateUtil.commitTransactionOrRollback();

            log.info("Finished inserting test data ...");
        }
        catch (Exception e) 
        {
            HibernateUtil.rollbackTransactionSilently();
            log.error("Inserting test data error: ", e);
        }
                
	Thread.currentThread().setName(oldThreadName);
    }

    @Override
    public void destroy()
    {
        super.destroy();
        
        try 
        {
            HibernateUtil.getSessionfactory().close();
        }
        catch (Exception e) 
        {
            log.error("Closing Hibernate SessionFactory error: ", e);
        }

        log.info("InitializationServlet: ########## DESTROY ##########");
    }
    
}
