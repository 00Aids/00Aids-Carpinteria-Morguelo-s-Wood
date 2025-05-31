package com.CarpinteriaSpringBoot.app.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.CarpinteriaSpringBoot.app.model.Proyecto;
import com.CarpinteriaSpringBoot.app.repository.ProyectoRepository;

@RestController
@RequestMapping("/api/proyectos")
public class ProyectoRestController {

    @Autowired
    private ProyectoRepository proyectoRepo;

    // Crear un nuevo proyecto
    @PostMapping
    public ResponseEntity<?> createProyecto(@RequestBody Proyecto proyecto) {
        try {
            Proyecto nuevoProyecto = proyectoRepo.save(proyecto);
            return new ResponseEntity<Proyecto>(nuevoProyecto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener todos los proyectos
    @GetMapping
    public ResponseEntity<?> getAllProyectos() {
        try {
            List<Proyecto> listaProyectos = proyectoRepo.findAll();
            return new ResponseEntity<List<Proyecto>>(listaProyectos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar un proyecto existente
    @PutMapping
    public ResponseEntity<?> updateProyecto(@RequestBody Proyecto proyecto) {
        try {
            Optional<Proyecto> proyectoOptional = proyectoRepo.findById(proyecto.getId());
            if (proyectoOptional.isPresent()) {
                Proyecto proyectoActualizado = proyectoRepo.save(proyecto);
                return ResponseEntity.ok(proyectoActualizado);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Proyecto no encontrado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getCause().toString());
        }
    }

    // Eliminar un proyecto por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProyecto(@PathVariable("id") String id) {
        try {
            proyectoRepo.deleteById(id);
            return new ResponseEntity<String>("Proyecto eliminado", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
