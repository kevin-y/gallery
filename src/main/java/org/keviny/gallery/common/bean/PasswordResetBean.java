package org.keviny.gallery.common.bean;

import java.io.Serializable;

public class PasswordResetBean implements Serializable {
	private static final long serialVersionUID = -4006882959893238003L;
	private String email;
	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
