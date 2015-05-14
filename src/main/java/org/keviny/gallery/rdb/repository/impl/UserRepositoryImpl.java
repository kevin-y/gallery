package org.keviny.gallery.rdb.repository.impl;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.keviny.gallery.common.QueryBean;
import org.keviny.gallery.common.Table;
import org.keviny.gallery.rdb.model.User;
import org.keviny.gallery.rdb.repository.UserRepository;
import org.keviny.gallery.util.EntityUtils;
import org.keviny.gallery.util.FieldUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Created by Kevin YOUNG on 2015/5/8.
 */
@Repository
@Transactional
public class UserRepositoryImpl extends RdbRespositorySupport<User> implements UserRepository {


	@Transactional(propagation = Propagation.REQUIRED)
	public void create(User user) {
		Assert.notNull(user);
		em.persist(user);
	}

	
/*	public User findByUsername(final QueryBean q) {
		Set<String> fields = q.getFields();
		List<String> fieldList = null;
		
		if(fields == null || fields.size() <= 0) {
			fieldList = FieldUtils.forgeFieldList(User.class);
		} else {
			fieldList = FieldUtils.forgeFieldList(fields, User.class);
		}
				
		StringBuilder jql = new StringBuilder();
		jql.append("SELECT ").append(FieldUtils.join(fieldList, "u"))
		   .append(" FROM ").append(TABLE_NAME).append(" u")
		   .append(" WHERE username=:username");
		List<Object[]> results = em.createQuery(jql.toString(), Object[].class)
		  .setParameter("username", q.getParams().get("username").toString())
		  .getResultList();
		
		return results.isEmpty() ? null : EntityUtils.getInstanceOf(results.get(0), fieldList, User.class);
	}*/

	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public User findByUsername(String username, Set<String> fields) {
		//String fieldString = FieldUtils.forgeFieldString(fields, User.class);
		return null;
	}
	
	/*@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public User findByEmail(String email) {
		String jpql = "SELECT u FROM " + TABLE_NAME + " u WHERE u.email=:email";
		Query q = em.createQuery(jpql, User.class);
		q.setParameter("email", email);
		List<?> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return (User) resultList.get(0);
		}
		return null;
	}
	*/
	

	@Transactional(propagation = Propagation.REQUIRED)
	public void remove(Integer id) {

	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void remove(String username) {

	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void removeByEmail(String email) {

	}

	@Override
	public Class<User> getEntityClass() {
		return User.class;
	}
}
