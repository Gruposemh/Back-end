package com.ong.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ong.backend.services.InscricaoService;

@RestController
@RequestMapping(value = "/inscricao")
public class InscricaoController {

	@Autowired
	InscricaoService inscricaoService;
	
	
}
