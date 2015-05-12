package org.keviny.gallery.rdb.repository;

import org.keviny.gallery.rdb.model.Account;

/**
 * Created by Kevin YOUNG on 2015/5/8.
 */

public interface AccountRepository {
	public void saveAccount(Account acc);
	public Account getAccountById(Integer id);
	public Account getAccountByUsername(String username);
}
