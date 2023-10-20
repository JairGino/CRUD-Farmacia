package com.pharmacy.repository;

import com.pharmacy.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByNameContainingIgnoreCase(@Param("name") String name);
}
