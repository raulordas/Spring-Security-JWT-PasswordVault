package com.raul.passwordvault.repositories;

import org.springframework.data.repository.CrudRepository;

import com.raul.passwordvault.dto.Usuario;

public interface RepositoryUsuarios extends CrudRepository<Usuario, Integer> {
	
	public Usuario findByUsername(String username);

	public Usuario findByEmail(String email);
}
