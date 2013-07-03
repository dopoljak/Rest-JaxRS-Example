package com.example.rest.jaxrsutils;

import java.io.InputStream;
import java.util.Properties;

/**
 * Simple utility for reading version from jar package 
 * 
 * @author DoDo <dopoljak@gmail.com>
 */
public class POMUtils
{
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(POMUtils.class);
    
    public static String getVersion(String groupId, String artifactId) 
    {
        final String pom_path = Strings.format("/META-INF/maven/{}/{}/pom.properties", groupId, artifactId);
        final Properties p = new Properties();

        try 
        {
            final InputStream is = POMUtils.class.getResourceAsStream(pom_path);
            p.load(is);
            return p.getProperty("version", "");
        }
        catch (Exception e) 
        {
            log.warn("POMUtils: Can't load version from artifactId : {}", artifactId);
        }

        try 
        {
            final InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(pom_path);
            p.load(is);
            return p.getProperty("version", "");
        }
        catch (Exception e) 
        {
            log.warn("ClassLoader: Can't load version from artifactId : {}", artifactId);
        }

        try 
        {
            final InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(pom_path);
            p.load(is);
            return p.getProperty("version", "");

        }
        catch (Exception e) 
        {
            log.warn("CurrentThread: Can't load version from artifactId : {}", artifactId);
        }

        return null;
    }
}
