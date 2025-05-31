package com.CarpinteriaSpringBoot.app.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Document(collection = "facturas")
public class Factura {

    @Id
    private String id;

    @DocumentReference
    private Cliente cliente;

    @DocumentReference
    private Proyecto proyecto;

    @DocumentReference
    private Mecanico mecanico;

    @DocumentReference
    private List<Repuesto> repuestos;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fecha;
    
    private double total; // ← ESTE CAMPO FALTABA

    public Factura() {
        this.fecha = new Date();
        this.total = 0.0; // Inicializar el total
    }

    // Getters y setters
    public String getId() { 
        return id; 
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public Cliente getCliente() { 
        return cliente; 
    }
    
    public void setCliente(Cliente cliente) { 
        this.cliente = cliente; 
    }

    public Proyecto getProyecto() { 
        return proyecto; 
    }
    
    public void setProyecto(Proyecto proyecto) { 
        this.proyecto = proyecto; 
    }

    public Mecanico getMecanico() { 
        return mecanico; 
    }
    
    public void setMecanico(Mecanico mecanico) { 
        this.mecanico = mecanico; 
    }

    public List<Repuesto> getRepuestos() { 
        return repuestos; 
    }
    
    public void setRepuestos(List<Repuesto> repuestos) { 
        this.repuestos = repuestos; 
    }

    public Date getFecha() { 
        return fecha; 
    }
    
    public void setFecha(Date fecha) { 
        this.fecha = fecha; 
    }

    public double getTotal() { 
        return total; 
    }
    
    public void setTotal(double total) { 
        this.total = total; 
    }
}
