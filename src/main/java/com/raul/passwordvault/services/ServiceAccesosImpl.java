package com.raul.passwordvault.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raul.passwordvault.dto.Acceso;
import com.raul.passwordvault.repositories.RepositoryAccesos;

@Component
public class ServiceAccesosImpl implements ServiceAccesos {
	
	@Autowired
	private RepositoryAccesos repositoryAccesos;

	@Override
	public Acceso saveAcceso(Acceso acceso) {
		return repositoryAccesos.save(acceso);
	}

	@Override
	public List<Acceso> findAllAccesos() {
		return (List<Acceso>) repositoryAccesos.findAll();
	}

	@Override
	public List<Acceso> findAllAccesosByUsuarioId(int id) {
		return repositoryAccesos.findAllByUsuarioId(id);
	}

	@Override
	public void deleteAccesoById(int id) {
		repositoryAccesos.deleteById(id);
	}

}
