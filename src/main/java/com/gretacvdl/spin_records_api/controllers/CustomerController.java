package com.gretacvdl.spin_records_api.controllers;

import com.gretacvdl.spin_records_api.dtos.CustomerDto;
import com.gretacvdl.spin_records_api.services.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        return ResponseEntity.ok(customerService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.findById(id));
    }

    @GetMapping("/email")
    public ResponseEntity<CustomerDto> getCustomerByEmail(@RequestParam String email) {
        return ResponseEntity.ok(customerService.findByEmail(email));
    }

    @PostMapping
    public ResponseEntity<CustomerDto> create(@RequestBody @Valid CustomerDto dto) {
        return ResponseEntity.ok(customerService.create(dto));
    }

    @PutMapping("/email/{email}")
    public ResponseEntity<CustomerDto> update(@PathVariable String email, @RequestBody @Valid CustomerDto dto) {
        return ResponseEntity.ok(customerService.update(email, dto));
    }

    @DeleteMapping("/email/{email}")
    public ResponseEntity<Void> delete(@PathVariable String email) {
        customerService.delete(email);
        return ResponseEntity.noContent().build();
    }
}