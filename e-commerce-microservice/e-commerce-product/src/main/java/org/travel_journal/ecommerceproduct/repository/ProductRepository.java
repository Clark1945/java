package org.travel_journal.ecommerceproduct.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.travel_journal.ecommerceproduct.entity.Product;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findById(Long id);
}
