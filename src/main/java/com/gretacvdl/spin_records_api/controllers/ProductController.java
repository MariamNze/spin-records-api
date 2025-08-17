package com.gretacvdl.spin_records_api.controllers;

import com.gretacvdl.spin_records_api.daos.ProductDao;
import com.gretacvdl.spin_records_api.dtos.ProductDto;
import com.gretacvdl.spin_records_api.entities.Product;
import com.gretacvdl.spin_records_api.mappers.ProductMapper;
import com.gretacvdl.spin_records_api.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @GetMapping("/search") // Type for ex "/search?keyword=nirv"
    public ResponseEntity<List<ProductDto>> searchProduct(@RequestParam String keyword) {
        List<ProductDto> response = productDao.searchByTitleOrArtist(keyword).stream().map(ProductMapper::toDto).toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ProductDto> create(@RequestBody ProductDto dto) {
        return ResponseEntity.ok(productService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable Long id, @RequestBody ProductDto dto) {
        return ResponseEntity.ok(productService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (productService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}