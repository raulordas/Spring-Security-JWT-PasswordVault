package com.raul.passwordvault.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.raul.passwordvault.dto.Acceso;
import com.raul.passwordvault.dto.Usuario;
import com.raul.passwordvault.models.JWTResponse;
import com.raul.passwordvault.security.CustomUserDetails;
import com.raul.passwordvault.security.JWTTokenUtil;
import com.raul.passwordvault.services.ServiceAccesosImpl;
import com.raul.passwordvault.services.ServiceUsuariosImpl;

@RequestMapping(path = "/usuarios")
@RestController
public class UsuariosController {
	
	@Autowired
	private ServiceUsuariosImpl serviceUsuarios;
	
	@Autowired
	private ServiceAccesosImpl serviceAccesos;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JWTTokenUtil tokenUtil;
	
	@Autowired
	private CustomUserDetails userDetails;
	
	
	@GetMapping(path = "/autenticar")
	public ResponseEntity<JWTResponse> obtenerToken(@RequestParam(required = true) String username, @RequestParam(required = true) String password) throws Exception {
		JWTResponse jwtResponse = null;
		UserDetails userAuth = null;
		
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			userAuth = userDetails.loadUserByUsername(username);
			
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
		
		Usuario userAuthenticated = serviceUsuarios.findByUsername(username);
		jwtResponse = new JWTResponse(tokenUtil, userAuth, userAuthenticated.getId());
		
		return new ResponseEntity<JWTResponse>(jwtResponse, HttpStatus.OK);
	}
	
	@PostMapping(path = "/signup")
	public ResponseEntity<Usuario> saveUsuario(@RequestBody Usuario usuario) {
		Usuario userResult = serviceUsuarios.saveUsuario(usuario);
		
		return new ResponseEntity<Usuario>(userResult, HttpStatus.OK);
	}
	
	@DeleteMapping(path = "/{usuario}")
	@Secured("ROLE_ADMIN")
	public ResponseEntity<Boolean> deleteUsuario(@PathVariable int usuario) {
		Usuario userDelete = serviceUsuarios.findUserById(usuario);
		
		if (userDelete != null) {
			userDelete.getAccesos().forEach(acceso -> {
				serviceAccesos.deleteAccesoById(acceso.getId());
			});	
			serviceUsuarios.deleteUsuarioById(userDelete.getId());
		}
		
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}
	
	
	//Método que persiste un acceso en la base de datos
	@Secured("ROLE_USER")
	@PostMapping(path = "/{usuario}/accesos")
	public ResponseEntity<Acceso> saveAcceso(@PathVariable int usuario, @RequestBody @Valid Acceso accesoSave) {
		
		//Chequeamos si el acceso se realiza sobre el usuario autenticado
		checkSecurityContextUsername(usuario);
		
		//Persistimos el acceso en el repositorio
		if (accesoSave.getUsuario() == null) {
			Usuario user = new Usuario();
			user.setId(usuario);
			accesoSave.setUsuario(user);
			
		} else {
			accesoSave.getUsuario().setId(usuario);
		}
		
		Acceso accesoResult = serviceAccesos.saveAcceso(accesoSave);
		
		return new ResponseEntity<Acceso>(accesoResult, HttpStatus.OK);
	}
	
	@GetMapping(path = "/finduser")
	public ResponseEntity<Boolean> findUsuarioByUsername(@RequestParam String username) {
		Usuario userResult = serviceUsuarios.findByUsername(username);
		if (userResult != null) {
			return new ResponseEntity<Boolean>(false, HttpStatus.OK);
		} else {
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		}
	}
	
	@GetMapping(path = "/findemail")
	public ResponseEntity<Boolean> findUsuarioByEmail(@RequestParam String email) {
		Usuario userResult = serviceUsuarios.findUserByEmail(email);
		
		if (userResult != null) {
			return new ResponseEntity<Boolean>(false, HttpStatus.OK);
		} else {
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		}
	}
	
	@Secured("ROLE_USER")
	@GetMapping(path = "/{usuario}/accesos")
	public ResponseEntity<List<Acceso>> findAllAccesosFromUser(@PathVariable int usuario) {
		
		//Chequeamos si el acceso se realiza sobre el usuario autenticado
		checkSecurityContextUsername(usuario);
		
		List<Acceso> accesosResult = serviceAccesos.findAllAccesosByUsuarioId(usuario);
		accesosResult.forEach(acceso -> acceso.setUsuario(null));
		
		return new ResponseEntity<List<Acceso>>(accesosResult, HttpStatus.OK);
	}
	
	@Secured("ROLE_USER")
	@DeleteMapping(path = "/{usuario}/accesos/{acceso}")
	public ResponseEntity<Boolean> deleteAccesoById(@PathVariable int usuario, @PathVariable int acceso) {
		
		//Chequeamos si el acceso se realiza sobre el usuario autenticado
		checkSecurityContextUsername(usuario);
		
		serviceAccesos.deleteAccesoById(acceso);
		
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}
	
	
	public Usuario checkSecurityContextUsername(int userId) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Usuario userAuth = serviceUsuarios.findByUsername(user.getUsername());
		
		if (userId != userAuth.getId()) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No dispone de Permisos sobre este recurso");
		}
		
		return userAuth;
	}
}
