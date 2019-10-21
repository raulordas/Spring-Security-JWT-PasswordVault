package com.raul.passwordvault.models;

import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;

import com.raul.passwordvault.security.JWTTokenUtil;

public class JWTResponse {
	private String username;
	private int id;
	private String token;
	private Date expiration_date;
	
	public JWTResponse() {}

	public JWTResponse(JWTTokenUtil tokenUtil, UserDetails userDetails, int id) {
		this.token = tokenUtil.generateToken(userDetails);
		this.username = tokenUtil.getUsernameFromToken(token);
		this.expiration_date = tokenUtil.getExpirationDateFromToken(token);
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getExpiration_date() {
		return expiration_date;
	}

	public void setExpiration_date(Date expiration_date) {
		this.expiration_date = expiration_date;
	}

	@Override
	public String toString() {
		return "JWTResponse [username=" + username + ", token=" + token + ", expiration_date=" + expiration_date + "]";
	}

}
