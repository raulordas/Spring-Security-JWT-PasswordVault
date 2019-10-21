package com.raul.passwordvault.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.raul.passwordvault.exceptions.CustomAuthenticationException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

@Component
public class JWTFilter extends OncePerRequestFilter {
	private static final String AUTHORIZATION = "Authorization";
	private static final String BEARER = "Bearer";
	
	@Autowired
	private CustomAuthenticationException excp;
	
	@Autowired
	private JWTTokenUtil tokenUtil;
	
	@Autowired
	private CustomUserDetails userDetails;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		excp.setStatus(null);
		excp.setReason(null);
		
		//Capturamos el header y comprobamos si comienza por Bearer. En caso afirmativo eliminamos dicho prefijo.
		String header = request.getHeader(AUTHORIZATION);
		
		if (header != null) {
			header = header.startsWith(BEARER)? header.substring(BEARER.length() + 1) : null; 
		}
		
		String user = null;
		
		
		//Si el token aparenta serlo al comenzar por bearer comprobamos su usuario
		if (header != null) {
			
			try {
				user = tokenUtil.getUsernameFromToken(header);
			
			} catch (IllegalArgumentException e) {
				excp.setReason("El Token suministrado no es válido");
				System.out.println("Unable to get JWT Token");
			
			} catch (ExpiredJwtException e) {
				excp.setReason("El token ha expirado para esta sesión");
				System.out.println("JWT Token has expired");
			
			} catch (MalformedJwtException e) {
				excp.setReason("El Token suministrado no es válido");
			}
		}
		
		//Si hemos obtenido un usuario del token y el contexto de seguridad es nulo
		if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			//Vamos a validar el token comenzando por autenticar al usuario
			UserDetails userCheck = userDetails.loadUserByUsername(user);
			
			//Intentamos validar el token con el usercheck
			if (tokenUtil.validateToken(header, userCheck)) {
				
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userCheck, null, userCheck.getAuthorities());
						usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						
						//Autenticamos definitivamente
						SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		
		filterChain.doFilter(request, response);
	}

}
