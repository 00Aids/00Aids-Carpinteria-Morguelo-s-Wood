package com.CarpinteriaSpringBoot.app.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CarpinteriaSpringBoot.app.model.Cliente;
import com.CarpinteriaSpringBoot.app.repository.ClienteRepository;

@RestController
@RequestMapping("/api/cliente")
public class ClienteRestController {
    
    @Autowired
    private ClienteRepository clienteRepo;

    @PostMapping
    public ResponseEntity<?> createCliente(@RequestBody Cliente cliente) {
        try {
            Cliente newCliente = clienteRepo.save(cliente);
            return new ResponseEntity<Cliente>(newCliente, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping
    public ResponseEntity<?> showAllClientes() {
        try {
            List<Cliente> listCliente = clienteRepo.findAll();
            return new ResponseEntity<List<Cliente>>(listCliente, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateCliente(@RequestBody Cliente cliente) {
        try {
            Optional<Cliente> clienteOptional = clienteRepo.findById(cliente.getId());
            if (clienteOptional.isPresent()) {
                Cliente updatedCliente = clienteRepo.save(cliente);
                return ResponseEntity.ok(updatedCliente);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente no encontrado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getCause().toString());
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteCliente(@PathVariable("id") String id) {
        try {
            clienteRepo.deleteById(id);
            return new ResponseEntity<String>("Cliente eliminado", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
/*
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Cliente cliente) {
        Cliente existeCliente = clienteRepo.findClienteByCedula(cliente.getCedula());
        if (existeCliente != null && existeCliente.getContraseña().equals(cliente.getContraseña())) {
            return ResponseEntity.ok("Inicio de sesión exitoso");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Cédula de cliente o contraseña incorrectos");
        }
    }*/
}
