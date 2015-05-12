package org.keviny.gallery.controller;

/**
 * Created by Kevin YOUNG on 2015/5/8.
 */

import javax.annotation.Resource;

import org.keviny.gallery.rdb.model.Account;
import org.keviny.gallery.rdb.repository.AccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/accounts")
public class AccountController {
	@Resource
	private AccountRepository accountRepository;
	
	@RequestMapping(
			value = "/{id}", 
			method = RequestMethod.GET
	)
	public ResponseEntity<Account> get(@PathVariable("id") Integer id ) {
		Account acc = accountRepository.getAccountById(id);
		HttpStatus status = acc == null ? HttpStatus.NOT_FOUND : HttpStatus.FOUND;
		return new ResponseEntity<Account>(acc, status);
	}
	
	/*@RequestMapping(
			value = "", 
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE
	)
	@ResponseStatus(value = HttpStatus.CREATED)
	public Account create(@Validated @RequestBody Account account) {
		
		
		
		accountRepository.saveAccount(account);
	}*/
}
