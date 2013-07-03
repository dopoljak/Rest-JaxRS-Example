package com.example.rest.database;

import com.example.rest.database.entity.Permission;
import com.example.rest.database.entity.Role;
import com.example.rest.database.entity.RolePermission;
import com.example.rest.database.entity.RolePermissionId;
import com.example.rest.database.entity.User;
import com.example.rest.database.entity.UserRole;
import com.example.rest.database.entity.UserRoleId;
import com.example.rest.database.entity.UserStatus;
import java.util.Enumeration;
import java.util.Properties;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * Utility class for Hibernate session/transaction manipulation
 * 
 * @author DoDo <dopoljak@gmail.com>
 */
public class HibernateUtil 
{
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(HibernateUtil.class);
    private static SessionFactory sessionfactory = null;

    public static SessionFactory getSessionfactory()
    {
	return sessionfactory;
    }

    /**
     * Get current session or open new one
     * 
     * @return
     */
    public static Session getCurrentSession()
    {
	Session session = sessionfactory.getCurrentSession();
	return session;
    }

    /**
     * Begin new transaction, use existing session bounded to the current
     * thread, autocommit mode is disabled, we need to commit transaction
     * manually after each use
     * 
     * @throws HibernateException
     */
    public static void beginTransaction() throws HibernateException
    {
	Session session = sessionfactory.getCurrentSession();
	// session.setFlushMode(FlushMode.MANUAL);
	session.beginTransaction();
    }

    /**
     * Commit current transaction. If there is error while committing,
     * auto-rollback and close current session.
     * 
     * @throws HibernateException
     */
    public static void commitTransaction() throws HibernateException
    {
	Transaction transaction = sessionfactory.getCurrentSession().getTransaction();
	try
	{
	    if (transaction != null && !transaction.wasCommitted() && !transaction.wasRolledBack() && transaction.isActive())
	    {
		transaction.commit();
	    }
	}
	catch (HibernateException e)
	{
	    throw e;
	}
    }

    /**
     * Rollback current transaction
     * 
     * @throws HibernateException
     */
    private static void rollbackTransaction() throws HibernateException
    {
	Transaction transaction = sessionfactory.getCurrentSession().getTransaction();
	try
	{
            if(transaction != null && transaction.isActive())
            {
                transaction.rollback();
            }
	}
	catch (HibernateException e)
	{
	    throw e;
	}
	finally
	{
	    closeSessionSilently();
	}
    }

    /**
     * Commit current transaction. If there is error while committing,
     * auto-rollback and close current session.
     * 
     * @throws HibernateException
     */
    public static void commitTransactionOrRollback() throws HibernateException
    {
	try
	{
	    commitTransaction();
	}
	catch (HibernateException e)
	{
	    rollbackTransaction();
	    throw e;
	}
    }

    /**
     * Because of Open-Session-In-View approach, this method is called after
     * each request/response to check if there is open transaction left by other
     * errors before commit is done
     */
    public static void rollbackTransactionSilently()
    {
	try
	{
	    rollbackTransaction();
	}
	catch (Exception e)
	{
	    log.error("Error while silent rollback transaction : ", e);
	}
    }

    public static void commitOrrollbackTransactionSilently()
    {
	try
	{
	    commitTransactionOrRollback();
	}
	catch (Exception e)
	{
	    log.error("Error while silent commit transaction : ", e);
	}
    }

    /**
     * Close current session silently
     */
    private static void closeSessionSilently()
    {
	Session session = sessionfactory.getCurrentSession();
	try
	{
	    if (session != null && session.isOpen())
	    {
		session.close();
	    }
	}
	catch (HibernateException e)
	{
	    log.error("Closing session after rollback error: ", e);
	}
    }

    public static void initialize(Properties properties) throws HibernateException
    {
	log.info("start: initialize() : hibernate connection");

	final Configuration config = new Configuration();

	// add annotated tables
	config.addAnnotatedClass(Permission.class);
        config.addAnnotatedClass(Role.class);
        config.addAnnotatedClass(RolePermission.class);
        config.addAnnotatedClass(RolePermissionId.class);
        config.addAnnotatedClass(User.class);
        config.addAnnotatedClass(UserRole.class);
        config.addAnnotatedClass(UserRoleId.class);
        config.addAnnotatedClass(UserStatus.class);

	// if trace mode is enabled	
	if(log.isTraceEnabled()) 
	{
	    config.setProperty(Environment.DRIVER, "com.p6spy.engine.spy.P6SpyDriver");
	    config.setProperty("org.hibernate.SQL", "trace" );
	    config.setProperty("org.hibernate.type", "trace" );
	}

	// Override properties
	Enumeration<?> enumeration = properties.propertyNames();
	while (enumeration.hasMoreElements())
	{
	    String key = (String) enumeration.nextElement();
	    String value = properties.getProperty(key);
	    config.setProperty(key, value);
	}

	ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(config.getProperties()).buildServiceRegistry();
	sessionfactory = config.buildSessionFactory(serviceRegistry);

	log.info("end: initialize() : hibernate connection");
    }
}
