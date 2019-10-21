package com.raul.passwordvault.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raul.passwordvault.dto.Usuario;
import com.raul.passwordvault.repositories.RepositoryUsuarios;

@Component
public class ServiceUsuariosImpl implements ServiceUsuarios {
	
	@Autowired
	private RepositoryUsuarios repositoryUsuarios;

	@Override
	public Usuario saveUsuario(Usuario usuario) {
		return repositoryUsuarios.save(usuario);
	}

	@Override
	public List<Usuario> findAllUsuarios() {
		return (List<Usuario>) repositoryUsuarios.findAll();
	}

	@Override
	public Usuario findByUsername(String username) {
		return repositoryUsuarios.findByUsername(username);
	}

	@Override
	public void deleteUsuarioById(int id) {
		repositoryUsuarios.deleteById(id);
	}

	@Override
	public Usuario findUserById(int id) {
		
		Optional<Usuario> optional = repositoryUsuarios.findById(id);
		
		if (optional.isPresent()) {
			return optional.get();
		} else {
			return null;
		}
	}

	@Override
	public Usuario findUserByEmail(String email) {
		return repositoryUsuarios.findByEmail(email);
	}
}
