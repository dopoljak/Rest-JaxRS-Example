package com.example.rest.database;

import ch.qos.logback.classic.Level;
import com.example.rest.database.dao.UserDAO;
import com.example.rest.database.entity.User;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import org.hibernate.cfg.Environment;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import com.example.rest.database.dao.PermissionDAO;
import com.example.rest.database.dao.RoleDAO;
import com.example.rest.database.dao.RolePermissionDAO;
import com.example.rest.database.dao.UserRoleDAO;
import com.example.rest.database.entity.Permission;
import com.example.rest.database.entity.Role;
import com.example.rest.database.entity.RolePermission;
import com.example.rest.database.entity.UserRole;
import com.example.rest.jaxrsutils.Hashs;
import com.example.rest.jaxrsutils.Strings;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;



/**
 *
 * @author dpoljak
 */
public class TestDatabase
{

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TestDatabase.class);
    public static final String DB_URL = "jdbc:h2:mem:test"; //"jdbc:h2:~/test";
    public static final String DB_USER = "sa";
    public static final String DB_PASS = "";
    public static final String DB_SCHEMA = "";
    //public static final String DB_DIALECT = "org.hibernate.dialect.H2Dialect";
    public static final String DB_DIALECT = "com.example.rest.database.ImprovedH2Dialect";
    public static final String DB_DRIVER = "org.h2.Driver";

    static void initialize() throws Exception
    {
        final Properties props = new Properties();
        props.setProperty(Environment.URL, DB_URL);
        props.setProperty(Environment.USER, DB_USER);
        props.setProperty(Environment.PASS, DB_PASS);
        //props.setProperty(Environment.DEFAULT_SCHEMA, DB_SCHEMA);
        props.setProperty(Environment.DIALECT, DB_DIALECT);
        props.setProperty(Environment.DRIVER, DB_DRIVER);

        // common configuration
        props.setProperty(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        props.setProperty(Environment.SHOW_SQL, "false");
        props.setProperty(Environment.FORMAT_SQL, "false");
        props.setProperty(Environment.POOL_SIZE, "10");
        props.setProperty(Environment.USE_SECOND_LEVEL_CACHE, "false");
        props.setProperty(Environment.C3P0_ACQUIRE_INCREMENT, "2");
        props.setProperty(Environment.C3P0_IDLE_TEST_PERIOD, "300");
        props.setProperty(Environment.C3P0_TIMEOUT, "1800");
        props.setProperty(Environment.AUTOCOMMIT, "false");

        // GENERATE SCHEMA ON THE FLY
        props.setProperty(Environment.FORMAT_SQL, "true");
        props.setProperty(Environment.HBM2DDL_AUTO, "create");

        try 
        {
            HibernateUtil.initialize(props);
        }
        catch (Exception e) {
            log.error("", e);
            throw e;
        }
    }

    
    public static void main(String args[]) throws Exception
    {
        
        Logger rootLogger = (Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        LoggerContext loggerContext = rootLogger.getLoggerContext();
        // we are not interested in auto-configuration
        loggerContext.reset();

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        //encoder.setPattern("%-5level [%thread]: %message%n");
        //encoder.setPattern("%d [%thread] %highlight(%-5level) %cyan(%logger{15}) - %msg %n");
        encoder.setPattern("%d [%thread] %highlight(%-5level) %cyan(%logger{0}) - %msg %n");
        encoder.start();

        ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<ILoggingEvent>();
        appender.setContext(loggerContext);
        appender.setEncoder(encoder); 
        appender.start();

        rootLogger.addAppender(appender);
        
        rootLogger.setLevel(Level.INFO);

        rootLogger.info("Message 0");
        rootLogger.debug("Message 1"); 
        rootLogger.warn("Message 2");
        
        
        
        
        
        
        
        
        
        
        
        


        System.out.println("...main ....");
        initialize();
        UserDAO userDAO = new UserDAO();


        
        
        HibernateUtil.beginTransaction();

        List<User> users = userDAO.findAll();

        for (User user : users) {
            log.info("#1#: User = {}", user);
        }

        HibernateUtil.commitTransactionOrRollback();
        
        
        
        
        
        
        

        HibernateUtil.beginTransaction();
        
        User newuser = new User();
        newuser.setFirstname("firstname");
        newuser.setCreated(new Date());
        userDAO.save(newuser);

        HibernateUtil.commitTransactionOrRollback();



        HibernateUtil.beginTransaction();

        users = userDAO.findAll();

        for (User user : users) {
            System.out.println("...main .... = " + user);
            log.info("#2#: User = {}", user);
        }

        HibernateUtil.commitTransactionOrRollback();

        
        
        
        
        
        
        
        
        
        HibernateUtil.beginTransaction();

        Permission perm1 = new Permission();
        perm1.setName("permisssion1");

        Role role1 = new Role();
        role1.setName("role1");
        role1.setCreated(new Date());
        
        RolePermission rolePermission1 = new RolePermission();
        rolePermission1.setPermission(perm1);
        rolePermission1.setRole(role1);
        
        PermissionDAO permDAO = new PermissionDAO();
        RoleDAO roleDAO = new RoleDAO();
        
        
        permDAO.save(perm1);
        log.info("created perm1 with id = {}", perm1.getId());
        
        
        roleDAO.save(role1);
        log.info("created role1 with id = {}", role1.getId());
        
        
        RolePermissionDAO rolePermissionDAO = new RolePermissionDAO();
        rolePermissionDAO.save(rolePermission1);

        HibernateUtil.commitTransactionOrRollback();

        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        HibernateUtil.beginTransaction();
        
        User newuser2 = new User();
        newuser2.setFirstname("firstname");
        newuser2.setCreated(new Date());
        userDAO.save(newuser2);
        log.info("created User with id = {}", newuser2.getId());
 
 
        
        UserRoleDAO urDAO = new UserRoleDAO();
        UserRole userRole = new UserRole();
        userRole.setUser(newuser2);
        userRole.setRole(role1);
        urDAO.save(userRole);
        log.info("created userRole with id = {}", userRole.toString());

        
        HibernateUtil.commitTransactionOrRollback();
        
        
        
        
        
        
        
        
        
        
        
        
        HibernateUtil.beginTransaction();

        users = userDAO.findAll();

        for (User user : users) 
        {
            log.info("#3#: User = {}, roles number = {}", user, user.getUserRoles().size());

            for (UserRole ur : user.getUserRoles()) 
            {
                log.info("#3#: UserRole = {}", ur);
            }
                    
        }

        HibernateUtil.commitTransactionOrRollback();
        
        
        
        System.out.println("...main ....");

    }
}
