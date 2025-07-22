package com.ong.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ong.backend.repositories.InscricaoRepository;

@Service
public class InscricaoService {

	@Autowired
	InscricaoRepository inscricaoRepository;
}
