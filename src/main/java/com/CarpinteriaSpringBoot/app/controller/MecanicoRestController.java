package com.CarpinteriaSpringBoot.app.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.CarpinteriaSpringBoot.app.model.Mecanico;
import com.CarpinteriaSpringBoot.app.repository.MecanicoRepository;

@RestController
@RequestMapping("/api/mecanicos")
public class MecanicoRestController {

    @Autowired
    private MecanicoRepository mecanicoRepo;

    // Crear un nuevo Mecanico
    @PostMapping
    public ResponseEntity<?> createMecanico(@RequestBody Mecanico mecanico) {
        try {
            Mecanico newMecanico = mecanicoRepo.save(mecanico);
            return new ResponseEntity<Mecanico>(newMecanico, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener todos los Mecanicos
    @GetMapping
    public ResponseEntity<?> showAllMecanicos() {
        try {
            List<Mecanico> listMecanico = mecanicoRepo.findAll();
            return new ResponseEntity<List<Mecanico>>(listMecanico, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar un Mecanico
    @PutMapping
    public ResponseEntity<?> updateMecanico(@RequestBody Mecanico mecanico) {
        try {
            Optional<Mecanico> mecanicoOptional = mecanicoRepo.findById(mecanico.getId());
            if (mecanicoOptional.isPresent()) {
                Mecanico updatedMecanico = mecanicoRepo.save(mecanico);
                return ResponseEntity.ok(updatedMecanico);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mecanico no encontrado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getCause().toString());
        }
    }

    // Eliminar un Mecanico por su ID
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteMecanico(@PathVariable("id") String id) {
        try {
            mecanicoRepo.deleteById(id);
            return new ResponseEntity<String>("Mecanico eliminado", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Método de login para el Mecanico (por cédula y contraseña)
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Mecanico mecanico) {
        Mecanico existeMecanico = mecanicoRepo.findByCedula(mecanico.getCedula());
        if (existeMecanico != null && existeMecanico.getContra().equals(mecanico.getContra())) {
            return ResponseEntity.ok("Inicio de sesión exitoso");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Cédula o contraseña incorrectos");
        }
    }
}

