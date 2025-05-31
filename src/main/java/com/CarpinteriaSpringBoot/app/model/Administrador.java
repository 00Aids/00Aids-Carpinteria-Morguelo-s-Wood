package com.CarpinteriaSpringBoot.app.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

@Document(collection = "administrador")
public class Administrador {

	  @Id
	    private String id;

	    private String cedula;

	    @NotEmpty(message = "El nombre es obligatorio")
	    private String nombre;
	    
	    @Email(message = "Correo electrónico no válido")
	    private String email;
	    
	    private String contra;

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

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getContra() {
			return contra;
		}

		public void setContra(String contra) {
			this.contra = contra;
		}
	    
	    
}
