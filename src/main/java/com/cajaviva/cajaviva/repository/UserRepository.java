package com.cajaviva.cajaviva.repository;

import com.cajaviva.cajaviva.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    // Puedes agregar más métodos de consulta
    // tener en cuenta la base de datos en esta 
}
