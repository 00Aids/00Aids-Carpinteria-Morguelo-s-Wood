package com.CarpinteriaSpringBoot.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.CarpinteriaSpringBoot.app.model.Cliente;
import com.CarpinteriaSpringBoot.app.repository.ClienteRepository;

@Controller
public class MecanicoController {

    @Autowired
    private ClienteRepository clienteRepository; // Accede directamente al repositorio

    @GetMapping("/indexmecanico")
    public String mostrarClientes(Model model) {
        // Obtener clientes con proyectos cargados explícitamente
    	List<Cliente> clientes = clienteRepository.findAll();
        
        model.addAttribute("clientes", clientes);
        return "indexmecanico";
    }



}
