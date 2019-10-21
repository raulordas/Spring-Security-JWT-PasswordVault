package com.raul.passwordvault.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationException {
	private HttpStatus status;
	private String reason;
	
	public CustomAuthenticationException() {}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	public String toString() {
		return "CustomAuthenticationException [status=" + status + ", reason=" + reason + "]";
	}
}
