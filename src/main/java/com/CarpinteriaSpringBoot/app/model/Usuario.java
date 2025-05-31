package com.CarpinteriaSpringBoot.app.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "usuarios")
public class Usuario {
    @Id
    private String id;
    
    private String email;
    private String password;
    private String rol;
    private String entidadId; // ID del cliente/carpintero relacionado
    private boolean primeraVez = true;

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getEntidadId() { return entidadId; }
    public void setEntidadId(String entidadId) { this.entidadId = entidadId; }

    public boolean isPrimeraVez() { return primeraVez; }
    public void setPrimeraVez(boolean primeraVez) { this.primeraVez = primeraVez; }
}
