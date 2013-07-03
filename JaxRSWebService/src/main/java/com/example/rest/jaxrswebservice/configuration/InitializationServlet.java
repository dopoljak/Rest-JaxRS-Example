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
import com.example.rest.jaxrsutils.POMUtils;
import com.example.rest.jaxrsutils.Strings;
import com.example.rest.jaxrswebservice.commons.Permissions;
import java.util.Date;
import java.util.Properties;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.hibernate.cfg.Environment;
import org.slf4j.LoggerFactory;

/**
 * Initialization servlet 
 * 
 * @author DoDo <dopoljak@gmail.com>
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

        // INITIALIZE LOGGER
        log.info("Initializing logger ...");
        initializeLogger();

        // INITIALIZE DATABASE
        log.info("Initializing database ...");
        initializeDatabase();

        // INSERT TEST DATA        
        log.info("Inserting test data ...");
        createAndInsertTestData();
        
        // GET MODULES VERSION
        String JaxRSWebService  = POMUtils.getVersion("com.example.rest", "JaxRSWebService");
        String JaxRSUtils       = POMUtils.getVersion("com.example.rest", "JaxRSUtils");
        String JaxRSDatabase    = POMUtils.getVersion("com.example.rest", "JaxRSDatabase");

	log.info("Started JaxRSWebService = {}, JaxRSUtils = {}, JaxRSDatabase = {}", new Object[] { JaxRSWebService, JaxRSUtils, JaxRSDatabase } );
	System.out.println("Started JaxRSWebService = " + JaxRSWebService + ", JaxRSUtils = " + JaxRSUtils + ", JaxRSDatabase = " + JaxRSDatabase);

	Thread.currentThread().setName(oldThreadName);
    }

    /**
     * Initialize Logback(SLF4J)
     */
    private void initializeLogger()
    {
        // ROOT LOGGER
        Logger rootLogger = (Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        LoggerContext loggerContext = rootLogger.getLoggerContext();
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
    }

    /**
     * Initialize database Hibernate + H2 in-memmory database
     */
    private void initializeDatabase()
    {
         // DB PROPERTIES
        final Properties dbProperties = new Properties() 
        {{
            setProperty(Environment.URL, "jdbc:h2:mem:test");
            setProperty(Environment.USER, "sa");
            setProperty(Environment.PASS, "");
            //setProperty(Environment.DEFAULT_SCHEMA, DB_SCHEMA);
            setProperty(Environment.DIALECT, "org.hibernate.dialect.H2Dialect");
            setProperty(Environment.DRIVER, "org.h2.Driver");

            // common configuration
            setProperty(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
            setProperty(Environment.SHOW_SQL, "false");
            setProperty(Environment.FORMAT_SQL, "false");
            setProperty(Environment.POOL_SIZE, "10");
            setProperty(Environment.USE_SECOND_LEVEL_CACHE, "false");
            setProperty(Environment.C3P0_ACQUIRE_INCREMENT, "2");
            setProperty(Environment.C3P0_IDLE_TEST_PERIOD, "300");
            setProperty(Environment.C3P0_TIMEOUT, "1800");
            setProperty(Environment.AUTOCOMMIT, "false");

            // GENERATE SCHEMA ON THE FLY
            setProperty(Environment.FORMAT_SQL, "true");
            setProperty(Environment.HBM2DDL_AUTO, "create");
        }};

        HibernateUtil.initialize(dbProperties);
    }

    /**
     * DEFAULT USERNAME/PASSWORD: dodo/dodo
     * 
     * Create and insert test data ( default user + password )
     */
    private void createAndInsertTestData()
    {
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
