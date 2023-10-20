package com.pharmacy.controller;

import com.pharmacy.model.Category;
import com.pharmacy.model.Product;
import com.pharmacy.repository.CategoryRepository;
import com.pharmacy.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping()
    public ResponseEntity<List<Product>> getAll(
            @RequestParam(value = "minPrice", defaultValue = "0") BigDecimal minPrice,
            @RequestParam(value = "maxPrice", defaultValue = "1000") BigDecimal maxPrice
    ) {
        if (minPrice.compareTo(maxPrice) > 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(productRepository.findAllByPriceBetween(minPrice, maxPrice));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(res -> ResponseEntity.ok(res))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/product/{name}")
    public ResponseEntity<List<Product>> getByName(@PathVariable String name) {
        return ResponseEntity.ok(productRepository.
                findAllByNameContainingIgnoreCase(name));
    }

    @PostMapping()
    public ResponseEntity<Product> register(@RequestBody @Valid Product product) {
        if (categoryRepository.existsById(product.getCategory().getId()))
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(productRepository.save(product));
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A categoria não existe!", null);
    }

    @PutMapping()
    public ResponseEntity<Product> updateById(@RequestBody @Valid Product product) {
        if (productRepository.existsById(product.getId())) {

            if (categoryRepository.existsById(product.getCategory().getId()))
                return productRepository.findById(product.getId())
                        .map(response -> ResponseEntity.status(HttpStatus.OK)
                                .body(productRepository.save(product)))
                        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A categoria não existe!", null);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        productRepository.deleteById(id);
    }

}
