package com.CarpinteriaSpringBoot.app.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

@Document(collection = "mecanicos")
public class Mecanico {

	  @Id
	    private String id;

	    private String cedula;

	    @NotEmpty(message = "El nombre es obligatorio")
	    private String nombre;

	    @Pattern(regexp = "^(\\+\\d{1,2}\\s)?\\d{10}$", message = "El teléfono debe tener 10 dígitos")
	    private String telefono;

	    private String especialidad;
	    
	    private String contra;
	    
	    private String nivelExperiencia;

	    private LocalDateTime fechaCreacion;
	     
	    private String direccion;
	    
	    
	    
	    
		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getCedula() {
			return cedula;
		}

		public void setCedula(String cedula) {
			this.cedula = cedula;
		}

		public String getNombre() {
			return nombre;
		}

		public void setNombre(String nombre) {
			this.nombre = nombre;
		}

		public String getTelefono() {
			return telefono;
		}

		public void setTelefono(String telefono) {
			this.telefono = telefono;
		}

		public String getEspecialidad() {
			return especialidad;
		}

		public void setEspecialidad(String especialidad) {
			this.especialidad = especialidad;
		}

		public String getContra() {
			return contra;
		}

		public void setContra(String contra) {
			this.contra = contra;
		}

		public String getNivelExperiencia() {
			return nivelExperiencia;
		}

		public void setNivelExperiencia(String nivelExperiencia) {
			this.nivelExperiencia = nivelExperiencia;
		}
		
		  public String getDireccion() {
			return direccion;
		}

		public void setDireccion(String direccion) {
			this.direccion = direccion;
		}

		public Mecanico() {
			    this.fechaCreacion = LocalDateTime.now();
			    
			    }
		public LocalDateTime getFechaCreacion() {
			return fechaCreacion;
		}

		public void setFechaCreacion(LocalDateTime fechaCreacion) {
			this.fechaCreacion = fechaCreacion;
		}
	    
	  
		  public String getFechaCreacionFormateada() {
		        // Formatear la fecha para mostrarla en un formato legible
		        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		        return fechaCreacion.format(formatter);
	    
}
}
