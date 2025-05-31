package com.CarpinteriaSpringBoot.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.CarpinteriaSpringBoot.app.model.Cliente;
import com.CarpinteriaSpringBoot.app.model.Usuario;
import com.CarpinteriaSpringBoot.app.repository.ClienteRepository;
import com.CarpinteriaSpringBoot.app.repository.ProyectoRepository;
import com.CarpinteriaSpringBoot.app.repository.UsuarioRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/clientes")
public class ClienteWebController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProyectoRepository proyectoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public String listarClientes(@RequestParam(value = "accion", required = false) String accion, Model model) {
        model.addAttribute("clientes", clienteRepository.findAll());
        model.addAttribute("accion", accion); 
        return "clientes";
    }

    @GetMapping("/nuevo")
    public String nuevoCliente(Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("proyectos", proyectoRepository.findAll());
        return "cliente_form";
    }

    @GetMapping("/editar/{id}")
    public String editarCliente(@PathVariable String id, Model model) {
        Cliente cliente = clienteRepository.findById(id).orElse(null);
        model.addAttribute("cliente", cliente);
        model.addAttribute("proyectos", proyectoRepository.findAll());
        return "cliente_form";
    }

    @PostMapping("/guardar")
    public String guardarCliente(@ModelAttribute Cliente cliente, RedirectAttributes redirectAttributes) {
        try {
            clienteRepository.save(cliente);

            // Crear usuario automático solo si no existe
            if (!usuarioRepository.existsByEmail(cliente.getEmail())) {
                Usuario usuario = new Usuario();
                usuario.setEmail(cliente.getEmail());
                usuario.setPassword(cliente.getCedula()); // ← Contraseña en texto plano
                usuario.setRol("CLIENTE");
                usuario.setEntidadId(cliente.getId());
                usuarioRepository.save(usuario);

                redirectAttributes.addFlashAttribute("mensaje", 
                    "Cliente y usuario creados exitosamente");
            } else {
                redirectAttributes.addFlashAttribute("error", 
                    "El email ya está registrado");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Error al guardar: " + e.getMessage());
        }
        return "redirect:/clientes";
    }


    @PostMapping("/actualizar/{id}")
    public String actualizarCliente(@PathVariable String id, @Valid @ModelAttribute Cliente cliente, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("proyectos", proyectoRepository.findAll());
            return "cliente_form";
        }
        cliente.setId(id);
        clienteRepository.save(cliente);
        return "redirect:/clientes";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable String id) {
        clienteRepository.deleteById(id);
        return "redirect:/clientes";
    }

    @PostMapping("/guardarNota/{id}")
    public String guardarNota(@PathVariable String id, @RequestParam("nota") String nota) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        cliente.setNota(nota);
        clienteRepository.save(cliente);
        return "redirect:/indexmecanico";
    }

    @GetMapping("/buscar")
    public String buscarCliente(@RequestParam("nombre") String nombre, Model model) {
        Cliente cliente = clienteRepository.findByNombre(nombre);
        if (cliente != null) {
            model.addAttribute("cliente", cliente);
            return "cliente_detalle";
        } else {
            model.addAttribute("mensaje", "Cliente no encontrado");
            return "clientes";
        }
    }
}
