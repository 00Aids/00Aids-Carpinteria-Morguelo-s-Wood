package com.CarpinteriaSpringBoot.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.CarpinteriaSpringBoot.app.model.Proyecto;

public interface ProyectoRepository extends MongoRepository<Proyecto, String> {
    List<Proyecto> findAll();
    
    Proyecto findByEstado(String placa);
    
    @Query("{ 'mecanico.$id' : { $oid: ?0 } }") // ← Conversión explícita a ObjectId
    List<Proyecto> findByMecanicoId(String mecanicoId);
    
    List<Proyecto> findByMecanicoIsNull();
    
    // Método heredado de MongoRepository, no necesita implementación
    Optional<Proyecto> findById(String id);

}
