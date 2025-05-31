package com.CarpinteriaSpringBoot.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;

import com.CarpinteriaSpringBoot.app.model.Proyecto;
import com.CarpinteriaSpringBoot.app.repository.ProyectoRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/proyectos")
public class ProyectoWebController {

    @Autowired
    private ProyectoRepository proyectoRepository;

    @GetMapping
    public String listarProyectos(Model model) {
        model.addAttribute("proyectos", proyectoRepository.findAll());
        return "proyectos";  // plantilla Thymeleaf para listar
    }

    @GetMapping("/nuevo")
    public String nuevoProyecto(Model model) {
        model.addAttribute("proyecto", new Proyecto());
        return "proyecto_form";  // plantilla Thymeleaf para formulario
    }

    @PostMapping("/guardar")
    public String guardarProyecto(@Valid @ModelAttribute Proyecto proyecto, BindingResult result) {
        if(result.hasErrors()) {
            return "proyecto_form";
        }
        proyectoRepository.save(proyecto);
        return "redirect:/proyectos";
    }

    @GetMapping("/editar/{id}")
    public String editarProyecto(@PathVariable("id") String id, Model model) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado: " + id));
        model.addAttribute("proyecto", proyecto);
        return "proyecto_form";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarProyecto(@PathVariable String id, @Valid @ModelAttribute Proyecto proyecto, BindingResult result) {
        if (result.hasErrors()) {
            return "proyecto_form";
        }
        proyecto.setId(id);
        proyectoRepository.save(proyecto);
        return "redirect:/proyectos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProyecto(@PathVariable("id") String id, Model model) {
        try {
            Proyecto proyecto = proyectoRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado: " + id));
            proyectoRepository.delete(proyecto);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "error"; // página de error
        }
        return "redirect:/proyectos";
    }
    
    @GetMapping("/detalle/{id}")
    public String verDetalleProyecto(@PathVariable String id, Model model) {
        Proyecto proyecto = proyectoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado: " + id));
        
        model.addAttribute("proyecto", proyecto);
        return "proyecto_detalle"; // Nombre de la nueva vista
    }

}
