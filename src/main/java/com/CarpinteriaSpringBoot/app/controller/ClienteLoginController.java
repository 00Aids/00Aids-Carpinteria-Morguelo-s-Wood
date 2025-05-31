package com.CarpinteriaSpringBoot.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.CarpinteriaSpringBoot.app.model.Cliente;
import com.CarpinteriaSpringBoot.app.repository.ClienteRepository;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cliente")
public class ClienteLoginController {

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping("/login")
    public String mostrarLoginCliente(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "login-cliente";
    }

    @PostMapping("/login")
    public String procesarLoginCliente(
        @RequestParam String email,
        @RequestParam String password,
        Model model,
        HttpSession session
    ) {
        // Buscar cliente por email
        Cliente clienteExistente = clienteRepository.findByEmail(email);
        
        if (clienteExistente != null && clienteExistente.getCedula().equals(password)) {
            session.setAttribute("clienteLogueado", clienteExistente);
            return "redirect:/cliente/dashboard";
        } else {
            model.addAttribute("error", "Email o cédula incorrectos");
            return "login-cliente";
        }
    }


    @GetMapping("/dashboard")
    public String dashboardCliente(HttpSession session, Model model) {
        Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");
        if (cliente == null) {
            return "redirect:/cliente/login";
        }
        model.addAttribute("cliente", cliente);
        return "dashboard-cliente";
    }

    @GetMapping("/logout")
    public String logoutCliente(HttpSession session) {
        session.removeAttribute("clienteLogueado");
        return "redirect:/cliente/login";
    }
}
