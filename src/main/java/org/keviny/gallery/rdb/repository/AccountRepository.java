package org.keviny.gallery.rdb.repository;

import org.keviny.gallery.rdb.model.Account;

/**
 * Created by Kevin YOUNG on 2015/5/8.
 */

public interface AccountRepository {
	void saveAccount(Account acc);
	Account getAccountById(Integer id);
	Account getAccountByUsername(String username);
}
