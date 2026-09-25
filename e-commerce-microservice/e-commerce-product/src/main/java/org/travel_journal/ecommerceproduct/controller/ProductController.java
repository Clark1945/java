package org.travel_journal.ecommerceproduct.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.travel_journal.ecommerceproduct.dto.ProductDTO;
import org.travel_journal.ecommerceproduct.dto.ProductUpdateDTO;
import org.travel_journal.ecommerceproduct.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO dto) {
        ProductDTO created = service.createProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductUpdateDTO dto) {
        ProductDTO updated = service.updateProduct(id, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
        ProductDTO dto = service.getProductById(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}/publish")
    public ResponseEntity<ProductDTO> publishProduct(@PathVariable Long id) {
        ProductDTO updated = service.publishProduct(id);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}/unpublish")
    public ResponseEntity<ProductDTO> unpublishProduct(@PathVariable Long id) {
        ProductDTO updated = service.unpublishProduct(id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        service.archiveProduct(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("API is working");
    }
}
