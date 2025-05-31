package com.CarpinteriaSpringBoot.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.CarpinteriaSpringBoot.app.model.Administrador;

public interface AdministradorRepository extends MongoRepository<Administrador, String> {
	Administrador findByCedula(String cedula);
	}
