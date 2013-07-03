package com.example.rest.jaxrswebservice.servlets;

public class InjectMe 
{
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(InjectMe.class);
    
    public String doWork() 
    {
        log.info("doWork() ...");
        return "injected object ...";
    }
}