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

import com.CarpinteriaSpringBoot.app.model.Factura;
import com.CarpinteriaSpringBoot.app.repository.FacturaRepository;

@RestController
@RequestMapping("/api/factura")
public class FacturaRestController {

    @Autowired
    private FacturaRepository facturaRepo;

    // Crear una nueva factura
    @PostMapping
    public ResponseEntity<?> createFactura(@RequestBody Factura factura) {
        try {
            Factura newFactura = facturaRepo.save(factura);
            return new ResponseEntity<Factura>(newFactura, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener todas las facturas
    @GetMapping
    public ResponseEntity<?> showAllFacturas() {
        try {
            List<Factura> listFactura = facturaRepo.findAll();
            return new ResponseEntity<List<Factura>>(listFactura, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar una factura existente
    @PutMapping
    public ResponseEntity<?> updateFactura(@RequestBody Factura factura) {
        try {
            Optional<Factura> facturaOptional = facturaRepo.findById(factura.getId());
            if (facturaOptional.isPresent()) {
                Factura updatedFactura = facturaRepo.save(factura);
                return ResponseEntity.ok(updatedFactura);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Factura no encontrada");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getCause().toString());
        }
    }

    // Eliminar una factura
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteFactura(@PathVariable("id") String id) {
        try {
            facturaRepo.deleteById(id);
            return new ResponseEntity<String>("Factura eliminada", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
