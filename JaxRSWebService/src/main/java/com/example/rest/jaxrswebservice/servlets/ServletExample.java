package com.example.rest.jaxrswebservice.servlets;

import com.example.rest.jaxrswebservice.interceptor.Intercepted;
import java.io.IOException;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * CDI Weld injection example
 * 
 * @author DoDo <dopoljak@gmail.com>
 */
@WebServlet( "/ServletExample" )
@Singleton
public class ServletExample extends HttpServlet 
{
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ServletExample.class);
    
    @Inject 
    InjectMe i;

    public ServletExample() 
    {
        super();
    }
    
    @Override
    @Intercepted
    protected void doGet(HttpServletRequest rq,HttpServletResponse rp) throws ServletException, IOException 
    {
        log.info("In doGet()");
        log.info("InjectMe work: {}", i.doWork());
    }
}