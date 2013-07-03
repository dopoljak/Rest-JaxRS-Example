package com.example.rest.jaxrswebservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 * Jackson JSON Jersey JAX-RS ObjectMapper Initialization
 * 
 * @author DoDo <dopoljak@gmail.com>
 */
@Provider
public class ObjectMapperProvider implements ContextResolver<ObjectMapper>
{
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ObjectMapperProvider.class);

    private final transient ObjectMapper objectMapper;

    public ObjectMapperProvider()
    {
	log.info("Initialized custom Jackson JSON ObjectMapper");
        System.out.println("Initialized custom Jackson JSON ObjectMapper");

	objectMapper = new ObjectMapper();
	objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        
    }

    @Override
    public ObjectMapper getContext(Class<?> arg0)
    {
	return objectMapper;
    }
}