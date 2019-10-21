package com.raul.passwordvault.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.raul.passwordvault.dto.Acceso;

public interface RepositoryAccesos extends CrudRepository<Acceso, Integer> {
	
	@Query(value = "SELECT * FROM accesos a WHERE a.usuario_id = :id", nativeQuery = true)
	public List<Acceso> findAllByUsuarioId(int id);

}
