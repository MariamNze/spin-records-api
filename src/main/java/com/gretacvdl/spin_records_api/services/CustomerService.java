package com.gretacvdl.spin_records_api.services;

import com.gretacvdl.spin_records_api.daos.CustomerDao;
import com.gretacvdl.spin_records_api.dtos.CustomerDto;
import com.gretacvdl.spin_records_api.entities.Customer;
import com.gretacvdl.spin_records_api.mappers.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerDao customerDao;

    public List<CustomerDto> findAll() {
        return customerDao.findAll().stream()
                .map(CustomerMapper::toDto)
                .collect(Collectors.toList());
    }

    public CustomerDto findById(Long id) {
        Customer customer = customerDao.findById(id);
        if (customer == null) {
            throw new RuntimeException("Client introuvable avec ID " + id);
        }
        return CustomerMapper.toDto(customer);
    }

    public CustomerDto findByEmail(String email) {
        Customer customer = customerDao.findByEmail(email);
        if (customer == null) {
            throw new RuntimeException("Client introuvable avec email " + email);
        }
        return CustomerMapper.toDto(customer);
    }

    public CustomerDto create(CustomerDto dto) {
        Customer customer = CustomerMapper.toEntity(dto);
        Customer created = customerDao.create(customer);
        return CustomerMapper.toDto(created);
    }

    public CustomerDto update(String email, CustomerDto dto) {
        Customer customer = CustomerMapper.toEntity(dto);
        Customer updated = customerDao.update(email, customer); // signature DAO existante
        if (updated == null) {
            throw new RuntimeException("Impossible de mettre à jour le client avec email " + email);
        }
        return CustomerMapper.toDto(updated);
    }

    public boolean delete(String email) {
        return customerDao.delete(email);
    }
}