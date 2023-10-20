package com.pharmacy.repository;

import com.pharmacy.model.Category;
import com.pharmacy.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByNameContainingIgnoreCase(@Param("name") String name);

    List<Product> findAllByPriceBetween(BigDecimal start, BigDecimal end);
}
