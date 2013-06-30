package com.example.rest.database.base;

import com.example.rest.database.HibernateUtil;
import java.io.Serializable;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

/**
 *
 * @author dpoljak
 */
public abstract class GenericDAO<T, ID extends Serializable>
{
    public Class<T> dataClass;

    public GenericDAO(Class<T> perClass)
    {
	this.dataClass = perClass;
    }

    /**
     * Get current Hibernate session
     */
    public Session getSession()
    {
	return HibernateUtil.getCurrentSession();
    }

    /**
     * Update Entity
     */
    public void update(T o)
    {
	getSession().update(o);
    }

    /**
     * Delete Entity
     */
    public void delete(T o)
    {
	getSession().delete(o);
    }

    /**
     * Save or Update Entity
     */
    public void saveOrUpdate(T o)
    {
	getSession().saveOrUpdate(o);
    }

    /**
     * Save Entity
     */
    public ID save(T o)
    {
	return (ID) getSession().save(o);
    }

    /**
     * Count Entities
     */
    public Long count()
    {
	return (Long) getSession().createCriteria(dataClass.getName()).setProjection(Projections.rowCount()).uniqueResult();
    }
    
    /**
     * Find Entity by ID
     */
    public T find(ID id)
    {
	return (T) getSession().get(dataClass, id);
    }

    /**
     * Get all Entities
     */
    public List<T> findAll()
    {
	Query query = getSession().createQuery("from " + dataClass.getName());
	return query.list();
    }

    /**
     * Get all Entities restricted by limit & offset and sorted by parameters
     */
    public List<T> find(Integer limit, Integer offset, String order, String sort)
    {
	Criteria criteria = getSession().createCriteria(dataClass);
	addLimitOffsetSortOrder(criteria, limit, offset, sort, order);
	return criteria.list();
    }
    
     /**
     * Get Entity by Example
     */
    public List<T> find(T example)
    {
	List<T> list = getSession().createCriteria(dataClass).add(Example.create(example)).list();
	return list;
    }

    /**
     * Add basic constraints
     */
    protected void addLimitOffsetSortOrder(Criteria criteria, Integer limit, Integer offset, String sort, String order)
    {
	if (limit != null)
	{
	    criteria.setMaxResults(limit);
	}
	if (offset != null)
	{
	    criteria.setFirstResult(offset);
	}

	if (order != null && sort != null)
	{
	    if (order.equalsIgnoreCase("asc"))
	    {
		criteria.addOrder(Order.asc(sort));
	    }
	    else if (order.equalsIgnoreCase("desc"))
	    {
		criteria.addOrder(Order.desc(sort));
	    }
	}
    }
}
