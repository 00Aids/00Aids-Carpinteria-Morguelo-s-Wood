package com.CarpinteriaSpringBoot.app.repository;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.CarpinteriaSpringBoot.app.model.Cliente;

public interface ClienteRepository extends MongoRepository<Cliente, String> {

    // Método para buscar un Cliente por su cédula
    Cliente findByCedula(String cedula);

    // Método para eliminar un Cliente por su cédula
    void deleteByCedula(String cedula);
    
    Cliente findByNombre(String nombre); 
    
 // Consulta para cargar proyectos automáticamente
    @Query(value = "{}", fields = "{'proyectos' : 1}")
    List<Cliente> findAllWithProyectos();
    
    Cliente findByEmail(String email);

}
