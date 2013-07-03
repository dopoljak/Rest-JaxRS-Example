package com.example.rest.database.base;

/**
 * 
 * @author DoDo <dopoljak@gmail.com>
 */
public class AuditField
{
    private int index;
    private Object value;

    public AuditField(int index, Object value)
    {
        this.index = index;
        this.value = value;
    }

    public int getIndex()
    {
        return index;
    }

    public Object getValue()
    {
        return value;
    }
    
    
}
