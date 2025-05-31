package com.CarpinteriaSpringBoot.app.repository;

import com.CarpinteriaSpringBoot.app.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    Usuario findByEmail(String email);
    boolean existsByEmail(String email);
}
