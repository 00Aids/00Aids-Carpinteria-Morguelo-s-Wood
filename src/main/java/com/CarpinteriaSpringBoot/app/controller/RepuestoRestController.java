package com.CarpinteriaSpringBoot.app.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.CarpinteriaSpringBoot.app.model.Repuesto;
import com.CarpinteriaSpringBoot.app.repository.RepuestoRepository;

@RestController
@RequestMapping("/api/repuestos")
public class RepuestoRestController {

    @Autowired
    private RepuestoRepository repuestoRepo;

    // Crear un nuevo Repuesto
    @PostMapping
    public ResponseEntity<?> createRepuesto(@RequestBody Repuesto repuesto) {
        try {
            Repuesto newRepuesto = repuestoRepo.save(repuesto);
            return new ResponseEntity<Repuesto>(newRepuesto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener todos los Repuestos
    @GetMapping
    public ResponseEntity<?> showAllRepuestos() {
        try {
            List<Repuesto> listRepuesto = repuestoRepo.findAll();
            return new ResponseEntity<List<Repuesto>>(listRepuesto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar un Repuesto
    @PutMapping
    public ResponseEntity<?> updateRepuesto(@RequestBody Repuesto repuesto) {
        try {
            Optional<Repuesto> repuestoOptional = repuestoRepo.findById(repuesto.getId());
            if (repuestoOptional.isPresent()) {
                Repuesto updatedRepuesto = repuestoRepo.save(repuesto);
                return ResponseEntity.ok(updatedRepuesto);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Repuesto no encontrado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getCause().toString());
        }
    }

    // Eliminar un Repuesto por su ID
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteRepuesto(@PathVariable("id") String id) {
        try {
            repuestoRepo.deleteById(id);
            return new ResponseEntity<String>("Repuesto eliminado", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /*
    // Método de login para el Repuesto (por código y nombre)
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Repuesto repuesto) {
        Repuesto existeRepuesto = repuestoRepo.findByCodigo(repuesto.getCodigo());
        if (existeRepuesto != null && existeRepuesto.getNombre().equals(repuesto.getNombre())) {
            return ResponseEntity.ok("Inicio de sesión exitoso");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Código o nombre incorrectos");
        }
    }*/
}

