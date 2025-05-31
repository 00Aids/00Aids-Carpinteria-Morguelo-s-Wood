package com.CarpinteriaSpringBoot.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CarpinteriaSpringBoot.app.model.Administrador;
import com.CarpinteriaSpringBoot.app.repository.AdministradorRepository;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/administradores")
public class AdminRestController {
    
    @Autowired
    private AdministradorRepository administradorRepo;

    // Crear un nuevo Administrador
    @PostMapping
    public ResponseEntity<?> createAdministrador(@RequestBody Administrador administrador) {
        try {
            Administrador newAdministrador = administradorRepo.save(administrador);
            return new ResponseEntity<Administrador>(newAdministrador, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Obtener todos los Administradores
    @GetMapping
    public ResponseEntity<?> showAllAdministradores() {
        try {
            List<Administrador> listAdministrador = administradorRepo.findAll();
            return new ResponseEntity<List<Administrador>>(listAdministrador, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar un Administrador
    @PutMapping
    public ResponseEntity<?> updateAdministrador(@RequestBody Administrador administrador) {
        try {
            Optional<Administrador> administradorOptional = administradorRepo.findById(administrador.getId());
            if (administradorOptional.isPresent()) {
                Administrador updatedAdministrador = administradorRepo.save(administrador);
                return ResponseEntity.ok(updatedAdministrador);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Administrador no encontrado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getCause().toString());
        }
    }

    // Eliminar un Administrador por su ID
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteAdministrador(@PathVariable("id") String id) {
        try {
            administradorRepo.deleteById(id);
            return new ResponseEntity<String>("Administrador eliminado", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Método de login para el Administrador (por cédula y contraseña)
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Administrador administrador) {
        Administrador existeAdministrador = administradorRepo.findByCedula(administrador.getCedula());
        if (existeAdministrador != null && existeAdministrador.getContra().equals(administrador.getContra())) {
            return ResponseEntity.ok("Inicio de sesión exitoso");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Cédula o contraseña incorrectos");
        }
    }
}
