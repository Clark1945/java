package org.travel_journal.ecommerceproduct.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.travel_journal.ecommerceproduct.entity.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
}

