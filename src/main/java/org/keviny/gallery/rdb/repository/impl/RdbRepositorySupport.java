package org.keviny.gallery.rdb.repository.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.keviny.gallery.common.Pagination;
import org.keviny.gallery.common.QueryBean;
import org.keviny.gallery.rdb.repository.RdbRepository;
import org.keviny.gallery.util.EntityUtils;
import org.keviny.gallery.util.FieldUtils;
import org.keviny.gallery.util.GenericsUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Kevin YOUNG on 2015/5/8.
 */

@Transactional
public abstract class RdbRepositorySupport<T> implements RdbRepository<T> {
	@PersistenceContext
	protected EntityManager entityManager;

	@SuppressWarnings("unchecked")
	protected final Class<T> entityClass = (Class<T>) GenericsUtils.getSuperClassGenricType(this.getClass());
	protected final String TABLE_NAME = EntityUtils.getEntityName(entityClass);

	public RdbRepositorySupport() {

	}
	
	/**
	 * Delete by primary key
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Serializable id) {
		entityManager.remove(entityManager.getReference(entityClass, id));
	}
	
	/**
	 *  Multi-deletion by primary keys
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteAll(Serializable... ids) {
		for (Serializable id : ids) {
			entityManager.remove(entityManager.getReference(entityClass, id));
		}
	}

	/**
	 *  Fetch by primary key
	 */
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public T get(Serializable id) {
		return entityManager.find(entityClass, id);
	}

	
	/*@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public T findOne(final String where, final Object... fields) {
		Query query = forgeQuery(null, null, where);
		int length = fields.length;
		for(int i = 0; i < length; i++) {
			query.setParameter(i, fields[i]);
		}
		query.setFirstResult(0);
		query.setMaxResults(1);
		List<?> results = query.getResultList();
		return (results.size() > 0 ? (T)results.get(0) : null);
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public T findOne(final String where, final Map<String, Object> fields) {
		Query query = forgeQuery(null, null, where);
		for(String field : fields.keySet()) {
			query.setParameter(field, fields.get(field));
		}
		query.setFirstResult(0);
		query.setMaxResults(1);
		List<?> results = query.getResultList();
		return (results.size() > 0 ? (T)results.get(0) : null);
	}*/
	
	private Query forgeQuery(final String prefix, final String ql ,final String where) {
		StringBuilder eql = null;
		if(ql == null || !"".equals(ql.trim()))
			eql = new StringBuilder("FROM " + TABLE_NAME + " AS o");
		else 
			eql = new StringBuilder(ql.trim());
		if(prefix != null)
			eql.insert(0, prefix + " ");
		if(where != null && !"".equals(where.trim()))
			eql.append(" " + where);
		return entityManager.createQuery(eql.toString(), entityClass);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void save(T entity) {
		entityManager.persist(entity);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void update(T entity) {
		entityManager.merge(entity);
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<T> pageList(Pagination page) {
		return pageList(page, null);
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<T> pageList(Pagination page, String eql) {
		Query countQuery = forgeQuery("SELECT COUNT(*)", eql, null);
		page.setTotalRecords(((Long) countQuery.getSingleResult()).longValue());
		Query query = entityManager.createQuery(eql);
		query.setFirstResult(page.getRecordBeginning().intValue());
		query.setMaxResults(page.getRecordsPerPage().intValue());
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<T> pageList(Pagination page, String eql,
			Map<String, Object> params) {
		Query countQuery = forgeQuery("SELECT COUNT(*)", eql, null);
		for (String key : params.keySet()) {
			countQuery.setParameter(key, params.get(key));
		}
		page.setTotalRecords(((Long) countQuery.getSingleResult()).longValue());
		Query query = entityManager.createQuery(eql);
		for (String key : params.keySet()) {
			query.setParameter(key, params.get(key));
		}
		query.setFirstResult(page.getRecordBeginning().intValue());
		query.setMaxResults(page.getRecordsPerPage().intValue());
		return query.getResultList();
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public long count() {
		return count(null);
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public long count(String eql) {
		Query countQuery = forgeQuery("SELECT COUNT(*)", eql, null);
		return (((Long) countQuery.getSingleResult()).longValue());
	}

	public long count(String eql, Map<String, Object> params) {
		Query countQuery = forgeQuery("SELECT COUNT(*)", eql, null);
		for (String key : params.keySet()) {
			countQuery.setParameter(key, params.get(key));
		}
		return (((Long) countQuery.getSingleResult()).longValue());
	}
}
