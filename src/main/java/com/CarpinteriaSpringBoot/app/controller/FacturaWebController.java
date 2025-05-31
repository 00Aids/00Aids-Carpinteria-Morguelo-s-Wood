package com.CarpinteriaSpringBoot.app.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.CarpinteriaSpringBoot.app.model.Factura;
import com.CarpinteriaSpringBoot.app.model.Cliente;
import com.CarpinteriaSpringBoot.app.model.Mecanico;
import com.CarpinteriaSpringBoot.app.model.Proyecto;
import com.CarpinteriaSpringBoot.app.model.Repuesto;
import com.CarpinteriaSpringBoot.app.repository.ClienteRepository;
import com.CarpinteriaSpringBoot.app.repository.FacturaRepository;
import com.CarpinteriaSpringBoot.app.repository.MecanicoRepository;
import com.CarpinteriaSpringBoot.app.repository.ProyectoRepository;
import com.CarpinteriaSpringBoot.app.repository.RepuestoRepository;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/facturas")
public class FacturaWebController {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private MecanicoRepository mecanicoRepository;

    @Autowired
    private RepuestoRepository repuestoRepository;

    // NUEVO: calcular total desde lista
    private double calcularTotalFacturaDesdeLista(List<Repuesto> repuestos) {
        double total = 0;
        for (Repuesto repuesto : repuestos) {
            total += repuesto.getPrecio() * repuesto.getCantidad();
        }
        return total;
    }

    @GetMapping
    public String listarFacturas(Model model) {
        model.addAttribute("facturas", facturaRepository.findAll());
        return "facturas";
    }

    @GetMapping("/nuevo")
    public String nuevaFactura(Model model) {
        model.addAttribute("factura", new Factura());
        cargarDatosFormulario(model);
        return "factura_form";
    }

    @PostMapping("/guardar")
    public String guardarFactura(@Valid @ModelAttribute Factura factura,
                                 BindingResult result,
                                 Model model,
                                 @RequestParam(value = "repuestosSeleccionados", required = false) List<String> repuestosSeleccionados,
                                 @RequestParam Map<String, String> allParams) {

        System.out.println(">>> ENTRANDO A guardarFactura");
        System.out.println("Cliente: " + (factura.getCliente() != null ? factura.getCliente().getId() : "null"));
        System.out.println("Proyecto: " + (factura.getProyecto() != null ? factura.getProyecto().getId() : "null"));
        System.out.println("Mecanico: " + (factura.getMecanico() != null ? factura.getMecanico().getId() : "null"));
        System.out.println("Fecha recibida: " + factura.getFecha());
        System.out.println("Repuestos seleccionados: " + repuestosSeleccionados);

        // Si hay errores de validación, mostrarlos y volver al formulario
        if (result.hasErrors()) {
            System.out.println("❌ Errores de validación:");
            result.getAllErrors().forEach(error -> System.out.println("  - " + error.getDefaultMessage()));
            cargarDatosFormulario(model);
            return "factura_form";
        }

        // Si no se proporciona fecha, usar la fecha actual
        if (factura.getFecha() == null) {
            factura.setFecha(new Date());
            System.out.println("📅 Fecha establecida automáticamente: " + factura.getFecha());
        }

        // Procesar repuestos seleccionados
        List<Repuesto> repuestosFinales = new ArrayList<>();
        double totalCalculado = 0.0;

        if (repuestosSeleccionados != null && !repuestosSeleccionados.isEmpty()) {
            for (String repuestoId : repuestosSeleccionados) {
                String cantidadKey = "cantidad-" + repuestoId;
                String cantidadStr = allParams.get(cantidadKey);
                
                System.out.println("Procesando repuesto ID: " + repuestoId + ", cantidad: " + cantidadStr);
                
                if (cantidadStr != null && !cantidadStr.trim().isEmpty()) {
                    try {
                        int cantidad = Integer.parseInt(cantidadStr.trim());
                        if (cantidad > 0) {
                            Optional<Repuesto> repuestoOpt = repuestoRepository.findById(repuestoId);
                            if (repuestoOpt.isPresent()) {
                                Repuesto repuestoOriginal = repuestoOpt.get();
                                
                                // Crear una copia del repuesto para la factura
                                Repuesto repuestoFactura = new Repuesto();
                                repuestoFactura.setId(repuestoOriginal.getId());
                                repuestoFactura.setNombre(repuestoOriginal.getNombre());
                                repuestoFactura.setPrecio(repuestoOriginal.getPrecio());
                                repuestoFactura.setCantidad(cantidad);
                                repuestoFactura.setDescripcion(repuestoOriginal.getDescripcion());
                                
                                repuestosFinales.add(repuestoFactura);
                                double subtotal = repuestoOriginal.getPrecio() * cantidad;
                                totalCalculado += subtotal;
                                
                                System.out.println("✅ Agregado: " + repuestoOriginal.getNombre() + 
                                                 " - Cantidad: " + cantidad + 
                                                 " - Precio: $" + repuestoOriginal.getPrecio() +
                                                 " - Subtotal: $" + subtotal);
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("❌ Error al parsear cantidad: " + cantidadStr);
                    }
                }
            }
        }

        // Asignar los repuestos y el total calculado
        factura.setRepuestos(repuestosFinales);
        factura.setTotal(totalCalculado);
        
        System.out.println("💰 Total calculado: $" + totalCalculado);
        System.out.println("📦 Repuestos procesados: " + repuestosFinales.size());

        try {
            Factura facturaGuardada = facturaRepository.save(factura);
            System.out.println("✅ Factura guardada exitosamente con ID: " + facturaGuardada.getId());
            return "redirect:/facturas?accion=agregado";
        } catch (Exception e) {
            System.out.println("❌ Error al guardar factura: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error al guardar la factura: " + e.getMessage());
            cargarDatosFormulario(model);
            return "factura_form";
        }
    }


    @GetMapping("/editar/{id}")
    public String editarFactura(@PathVariable String id, Model model) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Factura inválida: " + id));
        model.addAttribute("factura", factura);
        cargarDatosFormulario(model);
        return "factura_form";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarFactura(@PathVariable String id,
                                    @Valid @ModelAttribute Factura factura,
                                    BindingResult result,
                                    Model model,
                                    @RequestParam(value = "repuestosSeleccionados", required = false) List<String> repuestosSeleccionados,
                                    @RequestParam Map<String, String> allParams) {

        if (result.hasErrors()) {
            cargarDatosFormulario(model);
            return "factura_form";
        }

        factura.setId(id);

        // Usar la misma lógica de procesamiento de repuestos
        List<Repuesto> repuestosFinales = new ArrayList<>();
        double totalCalculado = 0.0;

        if (repuestosSeleccionados != null && !repuestosSeleccionados.isEmpty()) {
            for (String repuestoId : repuestosSeleccionados) {
                String cantidadKey = "cantidad-" + repuestoId;
                String cantidadStr = allParams.get(cantidadKey);
                
                if (cantidadStr != null && !cantidadStr.trim().isEmpty()) {
                    try {
                        int cantidad = Integer.parseInt(cantidadStr.trim());
                        if (cantidad > 0) {
                            Optional<Repuesto> repuestoOpt = repuestoRepository.findById(repuestoId);
                            if (repuestoOpt.isPresent()) {
                                Repuesto repuesto = repuestoOpt.get();
                                Repuesto repuestoFactura = new Repuesto();
                                repuestoFactura.setId(repuesto.getId());
                                repuestoFactura.setNombre(repuesto.getNombre());
                                repuestoFactura.setPrecio(repuesto.getPrecio());
                                repuestoFactura.setCantidad(cantidad);
                                repuestoFactura.setDescripcion(repuesto.getDescripcion());
                                
                                repuestosFinales.add(repuestoFactura);
                                totalCalculado += repuesto.getPrecio() * cantidad;
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Error al parsear cantidad para repuesto " + repuestoId + ": " + cantidadStr);
                    }
                }
            }
        }

        factura.setRepuestos(repuestosFinales);
        factura.setTotal(totalCalculado);
        facturaRepository.save(factura);

        return "redirect:/facturas?accion=editado";
    }


    @GetMapping("/eliminar/{id}")
    public String eliminarFactura(@PathVariable String id) {
        facturaRepository.deleteById(id);
        return "redirect:/facturas?accion=eliminado";
    }

    private void cargarDatosFormulario(Model model) {
        model.addAttribute("clientes", clienteRepository.findAll());
        model.addAttribute("proyectos", proyectoRepository.findAll());
        model.addAttribute("mecanicos", mecanicoRepository.findAll());
        model.addAttribute("repuestos", repuestoRepository.findAll());
    }
}
