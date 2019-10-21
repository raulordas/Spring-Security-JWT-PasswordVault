package com.raul.passwordvault.security;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.raul.passwordvault.dto.Usuario;
import com.raul.passwordvault.services.ServiceUsuariosImpl;

@Service
public class CustomUserDetails implements UserDetailsService {
	
	@Autowired
	ServiceUsuariosImpl serviceUsuarios;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario userAuth = serviceUsuarios.findByUsername(username);
		String role = "ROLE_" + userAuth.getRol().getRol().toString().toUpperCase();
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
		ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(authority);
		
		User user = new User(userAuth.getUsername(), userAuth.getPassword(), authorities);
		return user;
	}
	

}
