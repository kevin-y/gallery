package org.keviny.gallery.rdb.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.keviny.gallery.rdb.model.Account;
import org.keviny.gallery.rdb.repository.AccountRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Kevin YOUNG on 2015/5/8.
 */

@Repository
public class AccountRepositoryImpl implements AccountRepository {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void saveAccount(Account acc) {
		entityManager.persist(acc);
	}

	public Account getAccountById(Integer id) {
		Query q = entityManager.createNativeQuery("SELECT * FROM account WHERE id=:id", Account.class);
		q.setParameter("id", id);
		return (Account)q.getSingleResult();
	}

	@Override
	public Account getAccountByUsername(String username) {
		return null;
	}

}
