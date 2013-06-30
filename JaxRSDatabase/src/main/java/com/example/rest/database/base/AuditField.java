package com.example.rest.database.base;

/**
 *
 * @author DoDo
 */
public class AuditField
{
    public int index;
    public Object value;

    public AuditField(int index, Object value)
    {
        this.index = index;
        this.value = value;
    }
}
