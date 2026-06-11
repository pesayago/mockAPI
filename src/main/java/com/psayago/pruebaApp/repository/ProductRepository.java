package com.psayago.pruebaApp.repository;

import com.psayago.pruebaApp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de acceso a datos para la entidad {@link Product}.
 * Hereda las operaciones CRUD estandar de Spring Data JPA.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
