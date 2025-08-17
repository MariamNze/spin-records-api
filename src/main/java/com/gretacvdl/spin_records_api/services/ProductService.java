package com.gretacvdl.spin_records_api.services;

import com.gretacvdl.spin_records_api.daos.ProductDao;
import com.gretacvdl.spin_records_api.dtos.ProductDto;
import com.gretacvdl.spin_records_api.entities.Product;
import com.gretacvdl.spin_records_api.exceptions.ProductNotFoundException;
import com.gretacvdl.spin_records_api.mappers.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductDao productDao;

    public List<ProductDto> findAll(String s) {
        return productDao.findAll(s).stream()
                .map(ProductMapper::toDto)
                .collect(Collectors.toList());
    }

    public ProductDto findById(Long id) {
        Product product = productDao.findById(id);
        if (product == null) {
            throw new ProductNotFoundException("Produit introuvable avec ID " + id);
        }
        return ProductMapper.toDto(product);
    }

    public ProductDto create(ProductDto dto) {
        Product product = ProductMapper.toEntity(dto);
        Product created = productDao.create(product);
        return ProductMapper.toDto(created);
    }

    public ProductDto update(Long id, ProductDto dto) {
        Product product = ProductMapper.toEntity(dto);
        Product updated = productDao.update(id, product);
        if (updated == null) {
            throw new ProductNotFoundException("Impossible de mettre à jour le produit avec ID " + id);
        }
        return ProductMapper.toDto(updated);
    }

    public void delete(Long id) {
        boolean deleted = productDao.delete(id);
        if (!deleted) {
            throw new ProductNotFoundException("Impossible de supprimer : produit avec l'ID " + id + " est introuvable.");
        }
    }
}