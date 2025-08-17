package com.gretacvdl.spin_records_api.mappers;

import com.gretacvdl.spin_records_api.dtos.CustomerDto;
import com.gretacvdl.spin_records_api.entities.Customer;

public class CustomerMapper {

    public static CustomerDto toDto(Customer customer) {
        return new CustomerDto(
                customer.getId(),
                customer.getEmail(),
                customer.getName()
        );
    }

    public static Customer toEntity(CustomerDto dto) {
        return new Customer(
                dto.getId(),
                dto.getEmail(),
                dto.getName(),
                null // createdAt
        );
    }
}