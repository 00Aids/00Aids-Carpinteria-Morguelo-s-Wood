package com.CarpinteriaSpringBoot.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.CarpinteriaSpringBoot.app.model.Mecanico;
import com.CarpinteriaSpringBoot.app.model.Repuesto;
import com.CarpinteriaSpringBoot.app.repository.RepuestoRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/repuestos")
public class RepuestoWebController {
	@Autowired
    private RepuestoRepository repuestoRepository;

    @GetMapping
    public String listarRepuestos(Model model) {
        model.addAttribute("repuestos", repuestoRepository.findAll());
        return "repuestos";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioDeRepuesto(Model model) {
        model.addAttribute("repuesto", new Repuesto());
        return "repuesto_form";
    }

    @PostMapping("/guardar")
    public String guardarRepuesto(@Valid Repuesto repuesto, BindingResult result) {
        if (result.hasErrors()) {
            return "repuesto_form";
        }
        repuestoRepository.save(repuesto);
        return "redirect:/repuestos?accion=agregado";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarRepuesto(@PathVariable("id") String id, Model model) {
        Repuesto repuesto = repuestoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Repuesto no encontrado: " + id));
        model.addAttribute("repuesto", repuesto);
        return "repuesto_form";
    }
 // Actualizar un mecánico existente
    @PostMapping("/actualizar/{id}")
    public String actualizarRepuesto(@PathVariable String id, @Valid @ModelAttribute Repuesto repuesto, BindingResult result) {
        if (result.hasErrors()) {
            return "repuesto_form";
        }
        repuesto.setId(id);
        repuestoRepository.save(repuesto);
        return "redirect:/repuestos?accion=editado"; // Redirigir al listado con mensaje de éxito
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarRepuesto(@PathVariable("id") String id) {
        Repuesto repuesto = repuestoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Repuesto no encontrado: " + id));
        repuestoRepository.delete(repuesto);
        return "redirect:/repuestos?accion=eliminado";
    }
    
}
