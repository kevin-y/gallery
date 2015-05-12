package org.keviny.gallery.rdb.model;

/**
 * Created by Kevin YOUNG on 2015/5/8.
 */

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "account")
public class Account implements Serializable {
	private static final long serialVersionUID = -4890062561625166338L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@Column(name = "username", nullable = false, unique = true, length = 30)
	private String username;
	@Column(name = "passwd", nullable = false, length = 32)
	private String password;
	@Column(name = "login_ip", nullable = true, length = 20)
	private String loginIp;
	@Column(name = "login_time", nullable = true)
	private Date loginTime;
	@Column(name = "last_login_ip", nullable = true, length = 20)
	private String lastLoginIp;
	@Column(name = "last_login_time", nullable = true)
	private Date lastLoginTime;
	@Column(name = "locked", nullable = false, length = 1)
	private Boolean locked = false;
	@Column(name = "deleted", nullable = false, length = 1)
	private Boolean deleted = false;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

}
