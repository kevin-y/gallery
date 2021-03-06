package org.keviny.gallery.rdb.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.keviny.gallery.common.Pagination;
import org.keviny.gallery.common.QueryBean;

/**
 * Created by Kevin YOUNG on 2015/5/8.
 */

public interface RdbRepository<T> {
	/*void save(T entity);
	void update(T entity);
	void delete(Serializable id);
	void deleteAll(Serializable... ids);
	T get(Serializable id);
	List<T> pageList(Pagination page);
	List<T> pageList(Pagination page, String eql);
	List<T> pageList(Pagination page, String eql, Map<String, Object> params);
	long count();
	long count(String eql);
	long count(String eql, Map<String, Object> params);*/
	public T findOne(final QueryBean q);
	public List<T> find(final QueryBean q);
    public long count(final QueryBean q);
    public long count(final String jql);
    public long count(final String jql, Map<String, Object> params);
    public void updateSpecifiedFields(final QueryBean q, final Map<String, Object> values);
}
