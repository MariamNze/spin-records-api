package com.gretacvdl.spin_records_api.controllers;

import com.gretacvdl.spin_records_api.daos.ProductDao;
import com.gretacvdl.spin_records_api.dtos.ProductDto;
import com.gretacvdl.spin_records_api.entities.Product;
import com.gretacvdl.spin_records_api.mappers.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductDao productDao;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> response = productDao.findAll().stream().map(ProductMapper::toDto).toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        Product product = productDao.findById(id);
        ProductDto response = ProductMapper.toDto(product);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search") // Type for ex "/search?keyword=nirv"
    public ResponseEntity<List<ProductDto>> searchProduct(@RequestParam String keyword) {
        List<ProductDto> response = productDao.searchByTitleOrArtist(keyword).stream().map(ProductMapper::toDto).toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        Product product = ProductMapper.toEntity(productDto);
        Product created = productDao.create(product);
        ProductDto response = ProductMapper.toDto(created);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        Product product = ProductMapper.toEntity(productDto);
        Product updated = productDao.update(id, product);
        ProductDto response = ProductMapper.toDto(updated);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (productDao.delete(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}