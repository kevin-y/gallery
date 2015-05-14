package org.keviny.gallery.rdb.repository.impl;

import org.keviny.gallery.common.QueryBean;
import org.keviny.gallery.rdb.repository.RdbRepository;
import org.keviny.gallery.util.EntityUtils;
import org.keviny.gallery.util.FieldUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public T findOne(final QueryBean q) {
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
                .append(" FROM ").append(getTableName(entityClass)).append(" o")
                .append(" WHERE");

        for(String key : params.keySet()) {
            jql.append(" ").append(key).append("=:").append(key);
        }

        Query query = em.createQuery(jql.toString(), Object[].class);
        for(String key : params.keySet()) {
            query.setParameter(key, params.get(key));
        }
        List<Object[]> results = query.getResultList();
        return results.isEmpty() ? null : EntityUtils.getInstanceOf(results.get(0), fieldList, entityClass);
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
}
