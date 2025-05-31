package com.CarpinteriaSpringBoot.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.CarpinteriaSpringBoot.app.model.Administrador;
import com.CarpinteriaSpringBoot.app.repository.AdministradorRepository;

@Controller
@RequestMapping("administrador")
public class AdminWebController {
	   @Autowired
	    private AdministradorRepository administradorRepo;

	    // Formulario para agregar un nuevo administrador
	    @GetMapping("/agregar")
	    public String agregarAdministradorFormulario(Model model) {
	        model.addAttribute("administrador", new Administrador());
	        return "registro-administrador";
	    }

	    // Guardar un nuevo administrador en la base de datos
	    @PostMapping("/guardar")
	    public String guardarAdministrador(@ModelAttribute Administrador administrador) {
	        administradorRepo.save(administrador);
	        return "redirect:/administrador/inicio";
	    }

	    // Formulario para editar un administrador existente
	    @GetMapping("/editar/{cedula}")
	    public String editarAdministradorFormulario(@PathVariable String cedula, Model model) {
	        Administrador administrador = administradorRepo.findByCedula(cedula);
	        if (administrador != null) {
	            model.addAttribute("administrador", administrador);
	            return "registro-administrador";
	        } else {
	            return "redirect:/administrador/inicio";
	        }
	    }

	    // Borrar un administrador por su cédula
	    @GetMapping("/borrar/{cedula}")
	    public String borrarAdministradorFormulario(@PathVariable String cedula, Model model) {
	        administradorRepo.deleteById(cedula);
	        return "redirect:/administrador/inicio";
	    }
	    
	    @GetMapping("/")
		public String mostrarLogin() {
			return "login-administrador";
	    }
	    @GetMapping("/inicio")
		public String mostrarInicio(Model model) {
			model.addAttribute("administrador", administradorRepo.findAll());
				return "inicio-administrador";
	    }
	    
	    @PostMapping("/login")
		public String loginFormulario(@ModelAttribute Administrador administrador, Model model) {
	    	Administrador existsAdministrador = administradorRepo.findByCedula(administrador.getCedula());
			if (existsAdministrador != null && existsAdministrador.getContra().equals(administrador.getContra())) {
				model.addAttribute("administrador", administradorRepo.findAll());
				return "inicio-administrador";
			} else {
				model.addAttribute("mensaje", "Nombre de usuario o contraseña incorrectos");
				return "login-administrador";
			}
		}
	    
	    @GetMapping("/registro")
	    public String registroFormulario(Model model) {
	        model.addAttribute("administrador", new Administrador());
	        return "registro-administrador";
	    }

	    @PostMapping("/registro")
	    public String registrarAdministrador(@ModelAttribute Administrador administrador) {
	        administradorRepo.save(administrador);
	        return "redirect:/administrador/";
	    }
	    
	}

