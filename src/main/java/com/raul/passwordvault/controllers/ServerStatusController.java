package com.raul.passwordvault.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/status")
public class ServerStatusController {
	
	@GetMapping
	public String sendStatus() {
		return "El servidor está activo";
	}
}
