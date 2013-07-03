package com.example.rest.jaxrswebservice.interceptor;

import com.example.rest.database.base.AuditField;
import com.example.rest.database.base.IEntity;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * AuditLog & Validation interceptor for JaxRS services using Java CDI (Weld)
 * 
 * @author DoDo <dopoljak@gmail.com>
 */
@Intercepted
@Interceptor
public class MethodInterceptor
{
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MethodInterceptor.class);

    @AroundInvoke
    public Object intercept(InvocationContext context) throws Exception 
    {
        log.info("entry: intercept ...");

        for (Object object : context.getParameters()) 
        {
            if(object instanceof IEntity)
            {
                IEntity entity = (IEntity) object;
                
                // VALIDATE MANDATORY FIELDS
                entity.validateMandatoryFields();
                
                // AUDIT LOG FIELDS
                if(entity.getAuditFields() != null)
                {
                    for (AuditField auditField : entity.getAuditFields()) 
                    {
                        log.info("##### DB STORE AUDIT FIELDS: CLASS = {}, INDEX = {}, VALUE = {}", new Object[] { object.getClass().getName(), auditField.getIndex(), auditField.getValue() } );
                    }
                }
            }
        }
        
        Object result = context.proceed();
        
        log.info("exit: intercept ...");
        
        return result;
    }
}