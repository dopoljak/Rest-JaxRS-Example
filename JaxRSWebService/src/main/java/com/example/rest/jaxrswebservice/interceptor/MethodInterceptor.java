package com.example.rest.jaxrswebservice.interceptor;

import com.example.rest.database.base.IEntity;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Intercepted
@Interceptor
public class MethodInterceptor
{
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MethodInterceptor.class);

    @AroundInvoke
    public Object intercept(InvocationContext context) throws Exception 
    {
        log.info("entry: MethodInterceptor ... -> ... ");
        
        // REQUEST: validate mandatory ...
        for (Object object : context.getParameters()) 
        {
            if(object instanceof IEntity)
            {
                IEntity entity = (IEntity) object;
                entity.validateMandatoryFields();     // mandatory properties
                //entity.getAuditFields();            // audit fields
            }
        }
        
        Object result = context.proceed();
        
        log.info("exit: MethodInterceptor ...");
        
        return result;
    }
}