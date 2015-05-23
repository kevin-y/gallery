package org.keviny.gallery.rdb.repository.impl;

import org.keviny.gallery.common.QueryBean;
import org.keviny.gallery.rdb.repository.RdbRepository;
import org.keviny.gallery.util.EntityUtils;
import org.keviny.gallery.util.FieldUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.List;

/**
 * Created by Kevin YOUNG on 5/14/15.
 */
@Transactional
public abstract class RdbRespositorySupport<T> implements RdbRepository<T> {

    @PersistenceContext
    protected EntityManager em;

    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public T findOne(final QueryBean q) {
        List<T> list = find(q);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<T> find(final QueryBean q) {
        Set<String> fields= q.getFields();
        Map<String, Object> params = q.getParams();
        List<String> fieldList;
        Class<T> entityClass = getEntityClass();

        if(fields == null || fields.size() <= 0) {
            fieldList = FieldUtils.forgeFieldList(entityClass);
        } else {
            fieldList = FieldUtils.forgeFieldList(fields, entityClass);
        }

        // Build query string
        StringBuilder jql = new StringBuilder();
        jql.append("SELECT ").append(FieldUtils.join(fieldList, "o"))
                .append(" FROM ").append(getTableName(entityClass)).append(" o");

        boolean hasParams = false;

        if(params != null && !params.isEmpty()) {
            jql.append(" WHERE");
            int count  = 0;
            for(String key : params.keySet()) {
                jql.append(" ").append(key).append("=:").append(key).append(" AND");
                count++;
            }
            hasParams = count > 0;
            if(count > 0)
                jql.delete(jql.lastIndexOf("AND"), jql.length());
        }

        TypedQuery<Object[]> query = em.createQuery(jql.toString(), Object[].class);
        if(hasParams) {
            for(String key : params.keySet()) {
                query.setParameter(key, params.get(key));
            }
        }

        List<Object[]> results = query.getResultList();
        List<T> entityList = new ArrayList<T>();
        for(Object[] result : results) {
            T entity = EntityUtils.getInstanceOf(result, fieldList, entityClass);
            entityList.add(entity);
        }
        return entityList;
    }

    public abstract Class<T> getEntityClass();

    public String getTableName(Class<T> entityClass) {
        // if there's no Entity annotation exists, just return the class name
        if(!entityClass.isAnnotationPresent(Entity.class)) {
            return entityClass.getSimpleName();
        }
        Entity entityAnnotation = entityClass.getAnnotation(Entity.class);
        return entityAnnotation.name();
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public long count(final QueryBean q) {
        Map<String, Object> params = q.getParams();

        StringBuilder jql = new StringBuilder();
        jql.append("SELECT COUNT(*) FROM ")
                .append(getTableName(getEntityClass()));

        boolean hasParams = false;
        if(params != null && !params.isEmpty()) {
            jql.append(" WHERE");
            int i  = 0;
            for(String key : params.keySet()) {
                jql.append(" ").append(key).append("=:").append(key).append(" AND");
                i++;
            }
            hasParams = i > 0;
            if(i > 0)
                jql.delete(jql.lastIndexOf("AND"), jql.length());
        }

        TypedQuery<Long> query = em.createQuery(jql.toString(), Long.class);
        if(hasParams) {
            for(String key : params.keySet()) {
                query.setParameter(key, params.get(key));
            }
        }
        return query.getResultList().get(0);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public long count(final String jql) {
        return count(jql, null);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public long count(final String jql, Map<String, Object> params) {
        TypedQuery<Long> query = em.createQuery(jql,  Long.class);
        if(params != null) {
            for(String key : params.keySet()) {
                query.setParameter(key, params.get(key));
            }
        }
        return query.getResultList().get(0);
    }

    public void updateSpecifiedFields(final QueryBean q, final Map<String, Object> values) {
        Assert.notEmpty(values);
        StringBuilder jql = new StringBuilder("UPDATE ");
        //jql.append(getTableName(getEntityClass()))
        jql.append("users")
                .append(" SET ");

        for(String key : values.keySet()) {
            jql.append(key).append("=:").append(key).append(",");
        }
        // delete last comma
        jql.delete(jql.lastIndexOf(","), jql.length());


        Map<String, Object> params = q.getParams();
        if(params != null && !params.isEmpty()) {
            jql.append(" WHERE ");
            for(String key : params.keySet()) {
                // using `_` to distinguish params and values
                jql.append(key).append("=:_").append(key).append(" AND ");
            }
            jql.delete(jql.lastIndexOf("AND"), jql.length());
        }

        Query query = em.createQuery(jql.toString());
        for(String key : values.keySet()) {
            query.setParameter(key, values.get(key));
        }

        for(String key : params.keySet()) {
            query.setParameter("_" + key, params.get(key));
        }
        query.executeUpdate();
    }
}
