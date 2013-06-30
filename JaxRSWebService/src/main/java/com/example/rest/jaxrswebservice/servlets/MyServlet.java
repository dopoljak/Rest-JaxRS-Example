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

@WebServlet( "/MyServlet" )
@Singleton
public class MyServlet extends HttpServlet 
{
    @Inject 
    Injectee i;
    
    
    public MyServlet() 
    {
        super();
    }
    
    @Override
    @Intercepted
    protected void doGet(HttpServletRequest rq,HttpServletResponse rp) throws ServletException, IOException 
    {
        System.out.println("In doGet()");
        System.out.println("Injectee says: " + i.doWork());
    }
}