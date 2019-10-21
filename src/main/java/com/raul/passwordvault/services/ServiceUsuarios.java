package com.raul.passwordvault.services;

import java.util.List;
import com.raul.passwordvault.dto.Usuario;

public interface ServiceUsuarios {
	
	public Usuario saveUsuario(Usuario usuario);
	
	public List<Usuario> findAllUsuarios();
	
	public Usuario findByUsername(String username);
	
	public void deleteUsuarioById(int id);
	
	public Usuario findUserById(int id);
	
	public Usuario findUserByEmail(String email);
}
