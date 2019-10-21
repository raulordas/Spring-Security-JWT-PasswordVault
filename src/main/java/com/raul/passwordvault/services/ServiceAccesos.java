package com.raul.passwordvault.services;

import java.util.List;
import com.raul.passwordvault.dto.Acceso;

public interface ServiceAccesos {
	
	public Acceso saveAcceso(Acceso acceso);
	
	public List<Acceso> findAllAccesos();
	
	public List<Acceso> findAllAccesosByUsuarioId(int id);
	
	public void deleteAccesoById(int id);

}
