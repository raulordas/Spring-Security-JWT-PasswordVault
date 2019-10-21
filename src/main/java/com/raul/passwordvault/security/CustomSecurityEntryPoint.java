package com.raul.passwordvault.security;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raul.passwordvault.exceptions.CustomAuthenticationException;

@Component
public class CustomSecurityEntryPoint implements AuthenticationEntryPoint {
	
	@Autowired
	private CustomAuthenticationException excp;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		
		response.setContentType("application/json");
		
		System.out.println(excp.toString());
		if (excp.getReason() == null) {
			excp.setReason("Las credenciales facilitadas son incorrectas");
		}
		
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, excp.getReason());
		//OutputStream out = response.getOutputStream();
		//ObjectMapper mapper = new ObjectMapper();
		//mapper.writeValue(out, excp);
		//out.flush();
		
	}
}
