package com.CarpinteriaSpringBoot.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.CarpinteriaSpringBoot.app.model.Mecanico;

public interface MecanicoRepository extends MongoRepository<Mecanico, String> {
    // Consulta personalizada para buscar por cédula
    Mecanico findByCedula(String cedula);
    }
