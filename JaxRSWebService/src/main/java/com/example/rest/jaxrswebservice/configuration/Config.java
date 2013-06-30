package com.example.rest.jaxrswebservice.configuration;

import java.util.Properties;
import javax.servlet.ServletConfig;
import org.hibernate.cfg.Environment;

/**
 * @author dpoljak
 */
public class Config 
{
    // DEFAULT DB PROPERTIES
    private static final Properties DB_PROPERTIES = new Properties() 
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
    
    // DEFAULT LOG PROPETIES
    private static final Properties LOG_PROPERTIES = new Properties()
    {{
        
    }};

    public static void initialize(ServletConfig servletConfig) 
    {
                
    }

    public static Properties getDBProperties()
    {
        return DB_PROPERTIES;
    }
    
    public static Properties getLOGProperties()
    {
        return LOG_PROPERTIES;
    }
    
    public static Object getInitializedParameters() 
    {
        return "";
    }
}
