package org.keviny.gallery.rdb.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.keviny.gallery.common.Pagination;

/**
 * Created by Kevin YOUNG on 2015/5/8.
 */

public interface RdbRepository<T> {
	public void save(T entity);
	public void update(T entity);
	public void delete(Serializable id);
	public void deleteAll(Serializable... ids);
	public T get(Serializable id);
	public List<T> pageList(Pagination page);
	public List<T> pageList(Pagination page, String eql);
	public List<T> pageList(Pagination page, String eql, Map<String, Object> params);
	public long count();
	public long count(String eql);
	public long count(String eql, Map<String, Object> params);

}
