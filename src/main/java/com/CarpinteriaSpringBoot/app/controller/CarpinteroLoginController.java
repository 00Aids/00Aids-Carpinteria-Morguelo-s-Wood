package com.CarpinteriaSpringBoot.app.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.CarpinteriaSpringBoot.app.model.Mecanico;
import com.CarpinteriaSpringBoot.app.model.Proyecto;
import com.CarpinteriaSpringBoot.app.repository.MecanicoRepository;
import com.CarpinteriaSpringBoot.app.repository.ProyectoRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/carpintero")
public class CarpinteroLoginController {

    @Autowired
    private MecanicoRepository mecanicoRepository;
    
    @Autowired
    private ProyectoRepository proyectoRepository;

    @GetMapping("/login")
    public String mostrarLoginCarpintero(Model model) {
        model.addAttribute("mecanico", new Mecanico());
        return "login-carpintero";
    }

    @PostMapping("/login")
    public String procesarLoginCarpintero(
        @RequestParam String cedula,
        @RequestParam String password,
        Model model,
        HttpSession session
    ) {
        Mecanico mecanicoExistente = mecanicoRepository.findByCedula(cedula);
        
        if (mecanicoExistente != null && mecanicoExistente.getCedula().equals(password)) {
            session.setAttribute("carpinteroLogueado", mecanicoExistente);
            return "redirect:/carpintero/dashboard";
        } else {
            model.addAttribute("error", "Cédula o contraseña incorrectos");
            return "login-carpintero";
        }
    }


    @GetMapping("/dashboard")
    public String dashboardCarpintero(HttpSession session, Model model) {
        Mecanico carpintero = (Mecanico) session.getAttribute("carpinteroLogueado");
        if (carpintero == null) return "redirect:/carpintero/login";
        
     // Log para depuración
        System.out.println("ID del carpintero: " + carpintero.getId());
        
        List<Proyecto> proyectosAsignados = proyectoRepository.findByMecanicoId(carpintero.getId());
        List<Proyecto> proyectosDisponibles = proyectoRepository.findByMecanicoIsNull();
        
        // Calcular métricas
        long proyectosActivos = proyectosAsignados.stream()
            .filter(p -> "En curso".equals(p.getEstado()))
            .count();
            
        long proximasEntregas = proyectosAsignados.stream()
            .filter(p -> p.getFechaEntregaEstimada().isAfter(LocalDate.now()))
            .count();

        model.addAttribute("carpintero", carpintero);
        model.addAttribute("proyectosAsignados", proyectosAsignados);
        model.addAttribute("proyectosDisponibles", proyectosDisponibles);
        model.addAttribute("proyectosActivos", proyectosActivos);
        model.addAttribute("proximasEntregas", proximasEntregas);
        model.addAttribute("horasTrabajadas", 0); // Lógica pendiente
        
        return "dashboard-carpintero";
    }
    
    
    @PostMapping("/proyectos/asignar/{id}")
    public String asignarProyecto(
        @PathVariable String id, 
        HttpSession session
    ) {
        Mecanico carpintero = (Mecanico) session.getAttribute("carpinteroLogueado");
        if (carpintero == null) return "redirect:/carpintero/login";

        Proyecto proyecto = proyectoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));
        
        proyecto.setMecanico(carpintero);
        proyecto.setEstado("En curso");
        proyectoRepository.save(proyecto);
        
        return "redirect:/carpintero/dashboard";
    }



    @GetMapping("/logout")
    public String logoutCarpintero(HttpSession session) {
        session.removeAttribute("carpinteroLogueado");
        return "redirect:/carpintero/login";
    }
}
